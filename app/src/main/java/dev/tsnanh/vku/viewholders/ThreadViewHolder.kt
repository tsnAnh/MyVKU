/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.adapters.ThreadClickListener
import dev.tsnanh.vku.databinding.ItemThreadBinding
import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.utils.convertTimestampToDateString

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
            binding.lastUpdatedOn = lastUpdatedAt.convertTimestampToDateString()
        }
        binding.listener = listener
        binding.executePendingBindings()
    }
}