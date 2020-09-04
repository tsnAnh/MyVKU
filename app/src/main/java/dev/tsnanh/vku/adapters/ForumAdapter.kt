/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.domain.entities.NetworkForum
import dev.tsnanh.vku.viewholders.ForumViewHolder
import javax.inject.Inject

/**
 * A recycler view adapter for display list of Forum
 */
class ForumAdapter @Inject constructor(
    private val listener: ForumClickListener,
) : ListAdapter<NetworkForum, ForumViewHolder>(ForumWithLatestThreadDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ForumViewHolder.from(parent)

    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

// Click listener for each forum item
class ForumClickListener(val clickListener: (NetworkForum, MaterialCardView) -> Unit) {
    fun onClick(forum: NetworkForum, cardView: MaterialCardView) =
        clickListener(forum, cardView)
}

class ForumWithLatestThreadDiffUtil : DiffUtil.ItemCallback<NetworkForum>() {
    override fun areItemsTheSame(oldItem: NetworkForum, newItem: NetworkForum): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: NetworkForum, newItem: NetworkForum): Boolean {
        return oldItem.id == newItem.id
    }
}