package com.douyin

import android.app.Application
import android.content.Context
import com.zhy.http.okhttp.OkHttpUtils
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import android.support.multidex.MultiDex




class DouYinApp:Application() {

    override fun onCreate() {
        super.onCreate()

        val okHttpClient = OkHttpClient.Builder()
                //                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build()

        OkHttpUtils.initClient(okHttpClient)

    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}