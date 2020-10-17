package ru.quarter.images

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.io.InputStream
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

class DownloadService : IntentService("DownloadImages") {

    companion object {
        const val action_intent = "ru.quarter.images.RESPONSE"
    }

    val images: MutableMap<String, Bitmap> = ConcurrentHashMap()

    override fun onHandleIntent(intent: Intent?) {
        val url = intent!!.getStringExtra("url")!!
        if (images.containsKey(url)) {
            Log.w("Duplicated request", "Request duplication on $url")
            return
        }
        try {
            val stream: InputStream = URL(url).openStream()
            images[url] = BitmapFactory.decodeStream(stream)
            sendBroadcast(
                Intent()
                    .setAction(action_intent)
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .putExtra("url", url)
            )
        } catch (e: Exception) {
            Log.e("Error on image download", e.message!!)
            e.printStackTrace()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        onHandleIntent(intent)
        return DownloadBinder()
    }

    inner class DownloadBinder : Binder() {
        fun getDownloadService() = this@DownloadService
    }
}