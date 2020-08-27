/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.domain.entities.NetworkCustomForum
import dev.tsnanh.vku.viewholders.ForumViewHolder
import javax.inject.Inject

/**
 * A recycler view adapter for display list of Forum
 */
class ForumAdapter @Inject constructor(
    private var forums: List<NetworkCustomForum>,
    private val listener: ForumClickListener
) : RecyclerView.Adapter<ForumViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ForumViewHolder.from(parent)

    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        holder.bind(forums[position], listener)
    }

    override fun getItemCount() = forums.size
    fun updateForums(forums: List<NetworkCustomForum>) {
        this.forums = forums
        notifyDataSetChanged()
    }
}

// Click listener for each forum item
class ForumClickListener(val clickListener: (NetworkCustomForum, MaterialCardView) -> Unit) {
    fun onClick(forum: NetworkCustomForum, imageView: MaterialCardView) =
        clickListener(forum, imageView)
}