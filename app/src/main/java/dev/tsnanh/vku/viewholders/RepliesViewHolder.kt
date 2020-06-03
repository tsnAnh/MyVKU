/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.databinding.ItemReplyBinding
import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.utils.convertTimestampToDateString
import timber.log.Timber

class ReplyViewHolder private constructor(
    private val binding: ItemReplyBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ReplyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding =
                ItemReplyBinding.inflate(inflater, parent, false)
            return ReplyViewHolder(binding)
        }
    }

    fun bind(reply: Reply?) {
        Timber.d(reply.toString())
        reply?.let {
            binding.reply = it
            binding.timeCreated = it.createdAt.convertTimestampToDateString()
            binding.quotedReply = it.quotedReply
            Timber.d("${it.quotedReply}")
            binding.quotedTimeCreated =
                it.quotedReply?.createdAt?.convertTimestampToDateString()
            if (it.quotedReply != null) {
                binding.materialCardView.visibility = View.VISIBLE
            } else {
                binding.materialCardView.visibility = View.GONE
            }
        }
        binding.executePendingBindings()
    }
}