/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.domain.entities.NetworkForumThreadCustom
import dev.tsnanh.vku.viewholders.ThreadViewHolder

class ThreadAdapter(
    private val listener: ThreadClickListener
) : ListAdapter<NetworkForumThreadCustom, ThreadViewHolder>(ThreadDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ThreadViewHolder.from(parent)

    override fun onBindViewHolder(holder: ThreadViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

class ThreadClickListener(val clickListener: (NetworkForumThreadCustom, MaterialCardView) -> Unit) {
    fun onClick(thread: NetworkForumThreadCustom, cardView: View) =
        clickListener(thread, cardView as MaterialCardView)
}

class ThreadDiffUtil : DiffUtil.ItemCallback<NetworkForumThreadCustom>() {
    override fun areItemsTheSame(
        oldItem: NetworkForumThreadCustom,
        newItem: NetworkForumThreadCustom
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: NetworkForumThreadCustom,
        newItem: NetworkForumThreadCustom
    ): Boolean {
        return oldItem.id == newItem.id
    }
}