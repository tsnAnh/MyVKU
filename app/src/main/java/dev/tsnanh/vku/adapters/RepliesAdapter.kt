/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.tsnanh.vku.domain.entities.NetworkReply
import dev.tsnanh.vku.viewholders.ReplyViewHolder
import javax.inject.Inject

class RepliesAdapter @Inject constructor(
    private val uid: String,
    private val listener: ReplyClickListener,
) : ListAdapter<NetworkReply, ReplyViewHolder>(ReplyDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReplyViewHolder = ReplyViewHolder.from(parent)

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        holder.bind(uid, getItem(position), listener, position)
    }
}

class ReplyClickListener(
    val reply: (NetworkReply) -> Unit,
    val share: (NetworkReply) -> Unit,
) {
    fun onReply(reply: NetworkReply) = reply(reply)
    fun onShare(reply: NetworkReply) = share(reply)
}

class ReplyDiffUtil : DiffUtil.ItemCallback<NetworkReply>() {
    override fun areItemsTheSame(oldItem: NetworkReply, newItem: NetworkReply) =
        oldItem === newItem

    override fun areContentsTheSame(oldItem: NetworkReply, newItem: NetworkReply) =
        oldItem.id == newItem.id
}