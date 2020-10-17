package ru.quarter.images

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    val previews: MutableList<ImageEntry> = ArrayList()
    var ready = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewManager = LinearLayoutManager(this)
        previewRecycler.apply {
            layoutManager = viewManager
            adapter = ImageAdapter(previews) {
                if (ready) {
                    val intent = Intent(this@MainActivity, ActivityPreview::class.java)
                    intent.putExtra("url", it.url)
                    startActivity(intent)
                }
            }
        }

        DownloadImageTask(this).execute("https://picsum.photos/v2/list?limit=10")
    }
}