package ru.quarter.images

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.io.InputStream
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

class DownloadService : Service() {

    companion object {
        const val action_intent = "ru.quarter.images.RESPONSE"
    }

    val images: MutableMap<String, Bitmap> = ConcurrentHashMap()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        DownloadTask(this).execute(intent.getStringExtra("url"))
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        DownloadTask(this).execute(intent.getStringExtra("url"))
        return DownloadBinder()
    }

    inner class DownloadBinder : Binder() {
        fun getDownloadService() = this@DownloadService
    }

    class DownloadTask(private val service: DownloadService) : AsyncTask<String, Unit, Bitmap>() {

        private lateinit var url: String

        override fun doInBackground(vararg params: String?): Bitmap {
            Log.i("INTENT", "Started handling!")
            url = params[0]!!
            if (service.images.containsKey(url)) {
                Log.w("Duplicated request", "Request duplication on $url")
                return service.images[url]!!
            }
            try {
                println(url)
                val stream: InputStream = URL(url).openStream()
                return BitmapFactory.decodeStream(stream)
            } catch (e: Exception) {
                Log.e("ERR DOWNLOAD", "Oops...   :/")
                e.printStackTrace()
            }
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8)
        }

        override fun onPostExecute(result: Bitmap) {
            super.onPostExecute(result)
            service.images[url] = result
            service.sendBroadcast(
                Intent()
                    .setAction(action_intent)
                    .putExtra("url", url)
            )
            println("Broadcast sent")
        }
    }
}