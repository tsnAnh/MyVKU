/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.domain.entities.NetworkForumThread
import dev.tsnanh.vku.viewholders.ThreadViewHolder
import javax.inject.Inject

class ThreadAdapter @Inject constructor(
    private val uid: String,
    private val listener: ThreadClickListener,
) : ListAdapter<NetworkForumThread, ThreadViewHolder>(ThreadDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ThreadViewHolder.from(parent)

    override fun onBindViewHolder(holder: ThreadViewHolder, position: Int) {
        holder.bind(uid, getItem(position), listener, position)
    }
}

class ThreadClickListener(
    val clickListener: (NetworkForumThread, MaterialCardView) -> Unit,
) {
    fun onClick(thread: NetworkForumThread, cardView: View) =
        clickListener(thread, cardView as MaterialCardView)
}

class ThreadDiffUtil : DiffUtil.ItemCallback<NetworkForumThread>() {
    override fun areItemsTheSame(
        oldItem: NetworkForumThread,
        newItem: NetworkForumThread,
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: NetworkForumThread,
        newItem: NetworkForumThread,
    ): Boolean {
        return oldItem.id == newItem.id
    }
}