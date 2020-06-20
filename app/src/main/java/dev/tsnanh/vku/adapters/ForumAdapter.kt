/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.domain.entities.Forum
import dev.tsnanh.vku.viewholders.ForumViewHolder

/**
 * A recycler view adapter for display list of Forum
 */
class ForumAdapter(
    private var forums: List<Forum>,
    private val listener: ForumClickListener
) : RecyclerView.Adapter<ForumViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ForumViewHolder.from(parent)

    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        holder.bind(forums[position], listener)
    }

    override fun getItemCount() = forums.size
    fun updateForums(forums: List<Forum>) {
        this.forums = forums
        notifyDataSetChanged()
    }
}

// Click listener for each forum item
class ForumClickListener(val clickListener: (Forum, MaterialCardView) -> Unit) {
    fun onClick(forum: Forum, imageView: MaterialCardView) = clickListener(forum, imageView)
}