package com.douyin

import android.os.Bundle
import com.kotlinlib.KotlinActivity
import com.kotlinlib.other.LayoutId
import com.kotlinlib.utils.OnPageChange
import com.kotlinlib.view.FragPagerUtils
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import org.videolan.libvlc.LibVLC
import java.lang.Exception


@LayoutId(R.layout.activity_main)
class MainActivity : KotlinActivity() {

    companion object {
        var currentIndex = 0

    }

    lateinit var fragments: ArrayList<VideoFragment>

    var pageToken = ""

    var hasNext = false

    lateinit var fpUtils: FragPagerUtils<VideoFragment>
    //VLC库
    lateinit var libVLC: LibVLC

    override fun init(bundle: Bundle?) {
        libVLC = LibVLC(application)
        loading.start()
        OkHttpUtils
                .get()
                .url(BASE_URL)
                .addParams("uid", UID)
                .addParams("apikey", API_KEY)
                .build()
                .execute(object : StringCallback() {
                    override fun onResponse(response: String?, id: Int) {
                        loading.stop()
                        "response is $response".logD()
                        val data = gson.fromJson<Data>(response,Data::class.java)
                        hasNext = data.isHasNext
                        pageToken = data.pageToken
                        //创建媒体播放器
                        fragments = ArrayList(data.data.mapIndexed { index, dataBean ->
                            VideoFragment(index, dataBean.videoUrls[0])
                        })
                        fpUtils = FragPagerUtils(this@MainActivity,vpVideo,fragments)
                    }

                    override fun onError(call: Call?, e: Exception?, id: Int) {
                    }
                })

        var oldOffset = -1

        //监听翻页
        vpVideo.listenPageChange(object :OnPageChange{
            override fun onPageSelected(position: Int) {
                if(position-1>=0//确保不是第一个视频
                        &&!fragments[position-1].mediaPlayer.isReleased//如果上一个视频没有被释放
                        &&fragments[position-1].mediaPlayer.isPlaying//如果上一个视频正在播放
                ){
                    fragments[position-1].stop()//停止播放上一个视频
                }else{
                    fragments[position+1].stop()//停止播放下一个视频
                }
                fragments[position].play()//播放当前位置的视频
                //每翻三页请求一次网络数据
                if((position + 1)%3==0&&hasNext){
                    OkHttpUtils
                            .get()
                            .url(BASE_URL)
                            .addParams("uid", UID)
                            .addParams("apikey", API_KEY)
                            .addParams("pageToken",pageToken)
                            .build()
                            .execute(object : StringCallback() {
                                override fun onResponse(response: String?, id: Int) {
                                    val data = gson.fromJson<Data>(response,Data::class.java)
                                    hasNext = data.isHasNext
                                    pageToken = data.pageToken
                                    fragments.addAll(data.data.mapIndexed { index, dataBean ->
                                        VideoFragment(index+3, dataBean.videoUrls[0])
                                    })
                                    fpUtils.adapter.notifyDataSetChanged()
                                }

                                override fun onError(call: Call?, e: Exception?, id: Int) {
                                }
                            })
                }
            }

//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//                "$position------$positionOffset------$positionOffsetPixels".logD()
//                if(oldOffset!=-1&&positionOffsetPixels>oldOffset){
//                    fragments[position+1].play()//播放当前位置的视频
//                }
//                oldOffset = positionOffsetPixels
//            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        fragments[vpVideo.currentItem].stop()
    }

}
