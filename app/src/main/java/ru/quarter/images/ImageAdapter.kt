package ru.quarter.images

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.view.*

class ImageAdapter(
    private val images: List<ImageEntry>,
    private val onClick: (ImageEntry) -> Unit
): RecyclerView.Adapter<ImageEntryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageEntryViewHolder {
        val holder = ImageEntryViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item, parent, false)
        )

        holder.itemView.setOnClickListener {
            onClick(images[holder.adapterPosition])
        }

        return holder
    }

    override fun onBindViewHolder(holder: ImageEntryViewHolder, position: Int) = holder.bind(images[position])

    override fun getItemCount() = images.size
}

class ImageEntryViewHolder(root: View) : RecyclerView.ViewHolder(root) {
    fun bind(ImageEntry: ImageEntry) {
        with(itemView) {
            description.text = ImageEntry.description
        }
    }
}
