/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.viewholders.NewsViewHolder

class NewsAdapter(
    private val clickListener: NewsClickListener
) : ListAdapter<News, NewsViewHolder>(NewsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NewsViewHolder.from(parent)

    @ExperimentalStdlibApi
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, position)
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
        return oldItem.cmsId == newItem.cmsId
    }
}