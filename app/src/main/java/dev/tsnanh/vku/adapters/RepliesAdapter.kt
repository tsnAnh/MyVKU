/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.adapters.viewholder.RepliesViewHolder
import dev.tsnanh.vku.domain.Post

const val TYPE_FIRST = 0
const val TYPE_REPLIES = 1

class RepliesAdapter : ListAdapter<Post, RecyclerView.ViewHolder>(ReplyDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        TYPE_FIRST -> RepliesViewHolder.FirstReplyViewHolder.from(parent)
        else -> RepliesViewHolder.NormalReplyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RepliesViewHolder.FirstReplyViewHolder -> holder.bind(getItem(position))
            is RepliesViewHolder.NormalReplyViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_FIRST
            else -> TYPE_REPLIES
        }
    }
}


class ReplyDiffUtil : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem === newItem

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
}