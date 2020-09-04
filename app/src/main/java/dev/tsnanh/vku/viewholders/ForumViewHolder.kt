/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.adapters.ForumClickListener
import dev.tsnanh.vku.databinding.ItemForumBinding
import dev.tsnanh.vku.domain.entities.NetworkForum

class ForumViewHolder private constructor(
    private val binding: ItemForumBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ForumViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding =
                ItemForumBinding.inflate(inflater, parent, false)
            return ForumViewHolder(binding)
        }
    }

    fun bind(
        forum: NetworkForum,
        listener: ForumClickListener,
    ) {
        binding.forum = forum
        binding.cardForum.setOnClickListener {
            listener.onClick(forum, binding.cardForum)
        }
        binding.executePendingBindings()
    }
}