/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ReplyClickListener
import dev.tsnanh.vku.databinding.ItemReplyBinding
import dev.tsnanh.vku.domain.entities.NetworkReply
import dev.tsnanh.vku.utils.convertToDateString
import dev.tsnanh.vku.views.ReplyFragment

class ReplyViewHolder private constructor(
    private val binding: ItemReplyBinding,
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ReplyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding =
                ItemReplyBinding.inflate(inflater, parent, false)
            return ReplyViewHolder(binding)
        }
    }

    fun bind(uid: String, reply: NetworkReply, listener: ReplyClickListener, position: Int) {
        with(binding) {
            root.setOnCreateContextMenuListener { contextMenu, _, _ ->
                contextMenu.apply {
                    setHeaderTitle(root.context.getString(R.string.text_what_do_u_want_to_do))
                    if (uid == reply.uid?.uidGG) {
                        add(0, position, ReplyFragment.EDIT_ITEM_ORDER, "Edit")
                        add(0, position, ReplyFragment.DELETE_ITEM_ORDER, "Delete")
                    }
                    add(0,
                        position,
                        ReplyFragment.REPORT_ITEM_ORDER,
                        root.context.getString(R.string.text_report))
                }
            }
            this.reply = reply
            timeCreated = reply.createdAt.convertToDateString()
            quotedReply = reply.quoted
            quotedTimeCreated =
                reply.quoted?.replyId?.createdAt?.convertToDateString()
            if (reply.quoted != null) {
                materialCardView.visibility = View.VISIBLE
            } else {
                materialCardView.visibility = View.GONE
            }
            this.listener = listener
        }
        binding.executePendingBindings()
    }
}