/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters.viewholder.imagechooser

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.adapters.ImageChooserClickListener
import dev.tsnanh.vku.databinding.ItemImageChooserBinding

const val CLICK_TIME_INTERVAL: Long = 300
class ImageChooserViewHolder private constructor(
    private val binding: ItemImageChooserBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ImageChooserViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemImageChooserBinding
                .inflate(inflater, parent, false)

            return ImageChooserViewHolder(
                binding
            )
        }
    }

    private var lastClickTime = System.currentTimeMillis()
    fun bind(
        uri: Uri,
        listener: ImageChooserClickListener
    ) {
        binding.uri = uri
        binding.clearImage.setOnClickListener {
            val now = System.currentTimeMillis()
            if (now - lastClickTime < CLICK_TIME_INTERVAL) {
                return@setOnClickListener
            }
            lastClickTime = now
            listener.onClick(adapterPosition)
        }
        binding.executePendingBindings()
    }
}