/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.adapters.viewholders.ThreadViewHolder
import dev.tsnanh.vku.domain.ForumThread

class ThreadAdapter(
    private val listener: ThreadClickListener
) : ListAdapter<ForumThread, ThreadViewHolder>(ThreadDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ThreadViewHolder.from(parent)

    override fun onBindViewHolder(holder: ThreadViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

class ThreadClickListener(val clickListener: (ForumThread, MaterialCardView) -> Unit) {
    fun onClick(thread: ForumThread, cardView: View) =
        clickListener(thread, cardView as MaterialCardView)
}

class ThreadDiffUtil : DiffUtil.ItemCallback<ForumThread>() {
    override fun areItemsTheSame(oldItem: ForumThread, newItem: ForumThread): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ForumThread, newItem: ForumThread): Boolean {
        return oldItem.id == newItem.id
    }
}