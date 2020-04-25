/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters.viewholders.imagechooser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.adapters.ImageChooserClickListener
import dev.tsnanh.vku.databinding.ItemImageChooserFooterBinding

class ImageChooserFooterViewHolder private constructor(
    private val binding: ItemImageChooserFooterBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ImageChooserFooterViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding =
                ItemImageChooserFooterBinding.inflate(inflater, parent, false)
            return ImageChooserFooterViewHolder(binding)
        }
    }

    fun bind(listener: ImageChooserClickListener) {
        binding.listener = listener
        binding.executePendingBindings()
    }
}