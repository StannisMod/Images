package ru.quarter.images

import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import java.io.InputStream
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

open class DownloadImageTask(private val mainActivity: MainActivity) : AsyncTask<String, Unit, List<ImageEntry>>() {

    override fun doInBackground(vararg param: String?): List<ImageEntry> {
        val result: MutableList<ImageEntry> = ArrayList()
        try {
            val jsonStr: StringBuilder = StringBuilder()
            val stream: InputStream = URL(param[0]).openStream()
            val scanner = Scanner(stream)
            while (scanner.hasNext()) {
                jsonStr.append(scanner.nextLine())
            }
            val json: JsonArray = Gson().fromJson(jsonStr.toString(), JsonArray::class.java)
            json.forEach {
                result.add(ImageEntry(
                    it.asJsonObject.get("author").asString,
                    it.asJsonObject.get("url").asString))
            }
        } catch (e: Exception) {
            Log.e("Error on fetching data", e.message!!)
            //e.printStackTrace()
        }
        return result
    }

    override fun onPostExecute(result: List<ImageEntry>) {
        mainActivity.previews.addAll(result)
        mainActivity.ready = true
    }
}