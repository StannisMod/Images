package ru.quarter.images

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var previews: MutableList<ImageEntry> = ArrayList()
    var ready = false
    var getImagesTask : AsyncTask<String, Unit, List<ImageEntry>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            getImagesTask = DownloadImageTask(this).execute("https://picsum.photos/v2/list?limit=10")
        }
    }

    fun updatePreview() {
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("ready", ready)
        outState.putParcelable("previews", Storage(previews))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        ready = savedInstanceState.getBoolean("ready")
        previews = savedInstanceState.getParcelable<Storage>("previews")!!.list
        updatePreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        getImagesTask?.cancel(true)
    }

    class Storage(val list: MutableList<ImageEntry>) : Parcelable {

        constructor() : this(ArrayList())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeList(list)
        }

        override fun describeContents(): Int {
            return list.size
        }

        companion object CREATOR : Parcelable.Creator<Storage> {
            override fun createFromParcel(parcel: Parcel): Storage {
                val list = ArrayList<ImageEntry>()
                parcel.readList(list, List::class.java.classLoader)
                return Storage(list)
            }

            override fun newArray(size: Int): Array<Storage?> {
                return Array(size) { Storage() }
            }
        }
    }
}