/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.tsnanh.vku.domain.entities.NetworkCustomReply
import dev.tsnanh.vku.viewholders.ReplyViewHolder

class RepliesAdapter(
    private val uid: String,
    private val listener: ReplyClickListener
) : ListAdapter<NetworkCustomReply, ReplyViewHolder>(ReplyDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReplyViewHolder = ReplyViewHolder.from(parent)

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        holder.bind(uid, getItem(position), listener, position)
    }
}

class ReplyClickListener(
    val reply: (NetworkCustomReply) -> Unit,
    val share: (NetworkCustomReply) -> Unit
) {
    fun onReply(reply: NetworkCustomReply) = reply(reply)
    fun onShare(reply: NetworkCustomReply) = share(reply)
}

class ReplyDiffUtil : DiffUtil.ItemCallback<NetworkCustomReply>() {
    override fun areItemsTheSame(oldItem: NetworkCustomReply, newItem: NetworkCustomReply) =
        oldItem === newItem

    override fun areContentsTheSame(oldItem: NetworkCustomReply, newItem: NetworkCustomReply) =
        oldItem.id == newItem.id
}