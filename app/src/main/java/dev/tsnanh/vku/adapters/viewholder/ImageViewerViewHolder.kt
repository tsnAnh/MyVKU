/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.databinding.ItemImageViewerBinding
import timber.log.Timber

class ImageViewerViewHolder(
    private val binding: ItemImageViewerBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ImageViewerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding =
                ItemImageViewerBinding.inflate(inflater, parent, false)

            return ImageViewerViewHolder(binding)
        }
    }

    fun bind(url: String) {
        Timber.d(url)
        binding.url = url
        binding.executePendingBindings()
    }
}