package com.douyin

import android.annotation.SuppressLint
import android.graphics.SurfaceTexture
import android.net.Uri
import android.view.TextureView
import android.view.View
import com.kotlinlib.other.LayoutId
import com.kotlinlib.view.KotlinFragment
import kotlinx.android.synthetic.main.fragment_video.*
import org.videolan.libvlc.IVLCVout
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import java.util.ArrayList

@SuppressLint("ValidFragment")
@LayoutId(R.layout.fragment_video)
class VideoFragment(index:Int,url:String):KotlinFragment() {
    var index = index
    val testUrl = url

    override fun init(view: View?) {

    }

    override fun onResume() {
        super.onResume()
        tvVideo.surfaceTextureListener = object :TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                return false
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                if(MainActivity.currentIndex==index){
                    play()
                }
            }
        }
    }

    lateinit var mediaPlayer: MediaPlayer


    fun play(){
        //设置视频界面
        val act = activity as MainActivity
        //创建VLC库
        mediaPlayer = MediaPlayer(act.libVLC)
        //设置视频界面
        mediaPlayer.vlcVout.setVideoView(tvVideo)//效果同下句代码，也可以用SurfaceView
        //mediaPlayer.vlcVout.setVideoSurface(tvVideo.surfaceTexture)
        mediaPlayer.vlcVout.addCallback(object :IVLCVout.Callback{
            override fun onSurfacesCreated(vlcVout: IVLCVout?) {
                "界面创建了".logD()
            }

            override fun onSurfacesDestroyed(vlcVout: IVLCVout?) {
                "界面销毁了".logD()
            }
        })
        //将SurfaceView贴到MediaPlayer上
        //mediaPlayer.vlcVout.attachViews()
        mediaPlayer.vlcVout.attachViews()
        //设置播放窗口的尺寸
        mediaPlayer.vlcVout.setWindowSize(tvVideo.width, tvVideo.height)
        //设置媒体
        val media = Media(act.libVLC, Uri.parse(testUrl))
        mediaPlayer.media = media
        //播放
        mediaPlayer.play()
        mediaPlayer.setEventListener {
            when(it.type){
                MediaPlayer.Event.Stopped->{
                    //循环播放
                    mediaPlayer.media = media
                    mediaPlayer.play()
                }
            }
        }
    }


    fun stop() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

}