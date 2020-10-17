package ru.quarter.images

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class ImageEntry(val description: String, val url: String) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageEntry> {
        override fun createFromParcel(parcel: Parcel): ImageEntry {
            return ImageEntry(parcel)
        }

        override fun newArray(size: Int): Array<ImageEntry?> {
            return arrayOfNulls(size)
        }
    }
}