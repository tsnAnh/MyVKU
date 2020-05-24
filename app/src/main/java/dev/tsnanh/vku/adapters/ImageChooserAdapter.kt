/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.viewholders.imagechooser.ImageChooserFooterViewHolder
import dev.tsnanh.vku.viewholders.imagechooser.ImageChooserViewHolder

const val TYPE_FOOTER = 0
const val TYPE_ITEM = 1

class ImageChooserAdapter(
    private val listener: ImageChooserClickListener
) : ListAdapter<Uri, RecyclerView.ViewHolder>(UriDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_FOOTER -> ImageChooserFooterViewHolder.from(parent)
        TYPE_ITEM -> ImageChooserViewHolder.from(parent)
        else -> throw Exception("Invalid view holder")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageChooserFooterViewHolder -> holder.bind(listener)
            is ImageChooserViewHolder -> holder.bind(getItem(position), listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            currentList.size -> TYPE_FOOTER
            else -> TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return currentList.size.plus(1)
    }
}

class ImageChooserClickListener(val listener: (Int) -> Unit, val footerClick: () -> Unit) {
    fun onClick(position: Int) = listener(position)
    fun onFooterClick() = footerClick()
}

class UriDiffUtil : DiffUtil.ItemCallback<Uri>() {
    override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem.path == newItem.path
    }
}