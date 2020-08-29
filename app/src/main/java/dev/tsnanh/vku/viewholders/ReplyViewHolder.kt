/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.adapters.ReplyClickListener
import dev.tsnanh.vku.databinding.ItemReplyBinding
import dev.tsnanh.vku.domain.entities.NetworkCustomReply
import dev.tsnanh.vku.utils.convertToDateString
import dev.tsnanh.vku.views.ReplyFragment

class ReplyViewHolder private constructor(
    private val binding: ItemReplyBinding
) : RecyclerView.ViewHolder(binding.root)/*, View.OnCreateContextMenuListener */ {
    companion object {
        fun from(parent: ViewGroup): ReplyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding =
                ItemReplyBinding.inflate(inflater, parent, false)
            return ReplyViewHolder(binding)
        }
    }

    fun bind(uid: String, reply: NetworkCustomReply, listener: ReplyClickListener, position: Int) {
        reply.let {
            with(binding) {
                root.setOnCreateContextMenuListener { contextMenu, _, _ ->
                    contextMenu.apply {
                        setHeaderTitle("What do you want to do?")
                        if (uid == reply.uid?.uidGG) {
                            add(0, position, ReplyFragment.EDIT_ITEM_ORDER, "Edit")
                            add(0, position, ReplyFragment.DELETE_ITEM_ORDER, "Delete")
                        }
                        add(0, position, ReplyFragment.REPORT_ITEM_ORDER, "Report")
                    }
                }
                this.reply = it
                timeCreated = it.createdAt.convertToDateString()
                quotedReply = it.quoted
                quotedTimeCreated =
                    it.quoted?.replyId?.createdAt?.convertToDateString()
                if (it.quoted != null) {
                    materialCardView.visibility = View.VISIBLE
                } else {
                    materialCardView.visibility = View.GONE
                }
                this.listener = listener
            }
        }
        binding.executePendingBindings()
    }

    /*
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {

    }
    */
}