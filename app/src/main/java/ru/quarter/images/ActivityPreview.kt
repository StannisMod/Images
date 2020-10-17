package ru.quarter.images

import android.content.*
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_preview.*

class ActivityPreview : AppCompatActivity() {

    private lateinit var url: String
    private var image: Bitmap? = null

    private lateinit var service: DownloadService
    private var bound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        url = intent.getStringExtra("url")!!

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("url", url)
        if (image != null) {
            outState.putParcelable("image", image)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        url = savedInstanceState.getString("url")!!
        image = savedInstanceState.getParcelable("image")
    }

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
                image = service.images[url]!!
                preview.setImageBitmap(image)
            } else {
                println("Unbounded service!")
            }
        }
    }
}