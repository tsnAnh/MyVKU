/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.viewholders.ReplyViewHolder

class RepliesAdapter : ListAdapter<Reply, ReplyViewHolder>(ReplyDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReplyViewHolder = ReplyViewHolder.from(parent)

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class ReplyDiffUtil : DiffUtil.ItemCallback<Reply>() {
    override fun areItemsTheSame(oldItem: Reply, newItem: Reply) = oldItem === newItem

    override fun areContentsTheSame(oldItem: Reply, newItem: Reply) = oldItem.id == newItem.id
}