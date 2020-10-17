package ru.quarter.images

import android.content.*
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_preview.*

class ActivityPreview : AppCompatActivity() {

    private lateinit var url: String
    private lateinit var service: DownloadService
    private var bound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        url = savedInstanceState!!.getString("url")!!

        val filter = IntentFilter()
        filter.addCategory(DownloadService.action_intent)
        registerReceiver(MainBroadcastReceiver(), filter)

        bindService(
            Intent(this, DownloadService::class.java)
                .putExtra("url", url),
            mConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    /*fun startDownloading(urls: List<ImageEntry>) {
        urls.forEach {
            bindService(
                serviceIntent
                    .putExtra("description", it.description)
                    .putExtra("url", it.url),
                mConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }*/

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, mbinder: IBinder) {
            val binder: DownloadService.DownloadBinder = mbinder as DownloadService.DownloadBinder
            service = binder.getDownloadService()
            bound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            bound = false
        }
    }

    inner class MainBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            println("Received!!!")
            if (bound) {
                val url: String = intent.getStringExtra("url")!!
                preview.setImageBitmap(service.images[url])
            } else {
                println("Unbounded service!")
            }
        }
    }
}