/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.tsnanh.vku.adapters.viewholders.NewsViewHolder
import dev.tsnanh.vku.domain.News

class NewsAdapter(
    private val clickListener: NewsClickListener
) : ListAdapter<News, NewsViewHolder>(NewsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NewsViewHolder.from(parent)

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}

class NewsClickListener(
    val viewClickListener: (News) -> Unit,
    val shareClickListener: (News) -> Unit
) {
    fun onViewClick(news: News) = viewClickListener(news)
    fun onShareClick(news: News) = shareClickListener(news)
}

class NewsDiffUtil : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.url == newItem.url
    }
}