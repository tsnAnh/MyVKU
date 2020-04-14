package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.tsnanh.vku.adapters.viewholder.ForumViewHolder
import dev.tsnanh.vku.domain.Forum

class ForumAdapter(
    private val listener: ForumClickListener
) : ListAdapter<Forum, ForumViewHolder>(ForumDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ForumViewHolder.from(parent)

    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

class ForumClickListener(val clickListener: (Forum, ImageView) -> Unit) {
    fun onClick(forum: Forum, imageView: ImageView) = clickListener(forum, imageView)
}

class ForumDiffUtil : DiffUtil.ItemCallback<Forum>() {
    override fun areItemsTheSame(oldItem: Forum, newItem: Forum): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Forum, newItem: Forum): Boolean {
        return oldItem.id == newItem.id
    }
}