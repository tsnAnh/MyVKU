/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ThreadClickListener
import dev.tsnanh.vku.databinding.ItemThreadBinding
import dev.tsnanh.vku.domain.entities.NetworkForumThread
import dev.tsnanh.vku.utils.convertToDateString
import dev.tsnanh.vku.views.ThreadFragment

class ThreadViewHolder(
    private val binding: ItemThreadBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ThreadViewHolder {
            return ThreadViewHolder(
                ItemThreadBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    fun bind(
        uid: String,
        thread: NetworkForumThread,
        listener: ThreadClickListener,
        position: Int,
    ) {
        binding.apply {
            root.setOnCreateContextMenuListener { menu, _, _ ->
                menu.apply {
                    setHeaderTitle(root.context.getString(R.string.text_what_do_u_want_to_do))
                    if (uid == thread.uid.uidGG) {
                        add(0, position, ThreadFragment.EDIT_ITEM_ORDER, "Edit")
                        add(0, position, ThreadFragment.DELETE_ITEM_ORDER, "Delete")
                    }
                    add(0,
                        position,
                        ThreadFragment.REPORT_ITEM_ORDER,
                        root.context.getString(R.string.text_report))
                }
            }
            this.thread = thread
            createdAt = thread.createdAt.convertToDateString()
            this.listener = listener
            root.setOnLongClickListener {
                root.showContextMenu()
            }
        }.also { it.executePendingBindings() }
    }

}