/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.adapters.ThreadClickListener
import dev.tsnanh.vku.databinding.ItemThreadBinding
import dev.tsnanh.vku.domain.ForumThread
import dev.tsnanh.vku.utils.convertTimestampToDateString
import timber.log.Timber

class ThreadViewHolder(
    private val binding: ItemThreadBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup) =
            ThreadViewHolder(
                ItemThreadBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }

    fun bind(thread: ForumThread, listener: ThreadClickListener) {
        binding.thread = thread.apply {
            binding.createdAt = createdAt.convertTimestampToDateString()
            binding.lastUpdatedOn = lastUpdatedOn.convertTimestampToDateString()
            Timber.d(binding.createdAt)
            Timber.d(binding.lastUpdatedOn)
            Timber.d("$createdAt")
            Timber.d("$lastUpdatedOn")
        }
        binding.listener = listener
        binding.executePendingBindings()
    }
}