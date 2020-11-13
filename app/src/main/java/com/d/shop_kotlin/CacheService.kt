package com.d.shop_kotlin

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bumptech.glide.Glide
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class CacheService ():IntentService("CacheService"),AnkoLogger{
    companion object{
        val ACTION_CACHE_DONE = "action_cache_done"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onHandleIntent(intent: Intent?) {
        info("onHandleIntent")
//        Thread.sleep(5000)
        var title = intent?.getStringExtra("TITLE")
        var url = intent?.getStringExtra("URL")
        info("Downloaing...${title} ${url}")
        Glide.with(this)
            .download(url)
        sendBroadcast(Intent(ACTION_CACHE_DONE))
    }

    override fun onCreate() {
        super.onCreate()
        info("OnCreate")
    }

//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        info("onStartCommand")
//        return START_STICKY
////        return super.onStartCommand(intent, flags, startId)
//    }

    override fun onDestroy() {
        super.onDestroy()
        info("onDestroy")
    }

}