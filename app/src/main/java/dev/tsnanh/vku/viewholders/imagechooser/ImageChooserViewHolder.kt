/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewholders.imagechooser

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import dev.tsnanh.vku.adapters.ImageChooserClickListener
import dev.tsnanh.vku.databinding.ItemImageChooserBinding
import dev.tsnanh.vku.domain.network.BASE_URL
import timber.log.Timber

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
        Timber.i(uri.toString())
        Glide
            .with(binding.image)
            .load(uri)
            .placeholder(
                CircularProgressDrawable(binding.image.context)
            )
            .into(binding.image)
        binding.clearImage.setOnClickListener {
            val now = System.currentTimeMillis()
            if (now - lastClickTime < CLICK_TIME_INTERVAL) {
                return@setOnClickListener
            }
            lastClickTime = now
            listener.onClick(absoluteAdapterPosition)
        }
        binding.executePendingBindings()
    }
}

class UpdateReplyImageChooserViewHolder private constructor(
    private val binding: ItemImageChooserBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): UpdateReplyImageChooserViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemImageChooserBinding
                .inflate(inflater, parent, false)

            return UpdateReplyImageChooserViewHolder(
                binding
            )
        }
    }

    private var lastClickTime = System.currentTimeMillis()
    fun bind(
        uri: String,
        listener: ImageChooserClickListener
    ) {
        Timber.d(uri)
        Glide
            .with(binding.image)
            .load("${BASE_URL}/images/$uri")
            .placeholder(
                CircularProgressDrawable(binding.image.context)
            )
            .into(binding.image)
        binding.clearImage.setOnClickListener {
            val now = System.currentTimeMillis()
            if (now - lastClickTime < CLICK_TIME_INTERVAL) {
                return@setOnClickListener
            }
            lastClickTime = now
            listener.onClick(absoluteAdapterPosition)
        }
        binding.executePendingBindings()
    }
}
