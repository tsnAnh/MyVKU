/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.domain.Forum
import dev.tsnanh.vku.viewholders.ForumViewHolder

/**
 * A recycler view adapter for display list of Forum
 */
class ForumAdapter(
    private val listener: ForumClickListener
) : ListAdapter<Forum, ForumViewHolder>(ForumDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ForumViewHolder.from(parent)

    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

// Click listener for each forum item
class ForumClickListener(val clickListener: (Forum, MaterialCardView) -> Unit) {
    fun onClick(forum: Forum, imageView: MaterialCardView) = clickListener(forum, imageView)
}

/**
 * ForumDiffUtil for ForumAdapter
 */
class ForumDiffUtil : DiffUtil.ItemCallback<Forum>() {
    override fun areItemsTheSame(oldItem: Forum, newItem: Forum): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Forum, newItem: Forum): Boolean {
        return oldItem.id == newItem.id
    }
}