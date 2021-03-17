/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.myvku.views.news.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import dev.tsnanh.myvku.base.BaseRecyclerViewAdapter
import dev.tsnanh.myvku.base.OnListStateChangeListener
import dev.tsnanh.myvku.domain.entities.News
import javax.inject.Inject

class NewsAdapter @Inject constructor(
    private val clickListener: NewsClickListener,
    onListStateChangeListener: OnListStateChangeListener
) : BaseRecyclerViewAdapter<News, NewsViewHolder>(NewsDiffUtil(), onListStateChangeListener) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NewsViewHolder.from(parent)

    @ExperimentalStdlibApi
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, position)
    }
}

class NewsClickListener(
    val viewClickListener: (News) -> Unit,
    val shareClickListener: (News) -> Unit,
    val onPressListener: (News) -> Boolean,
) {
    fun onViewClick(news: News) = viewClickListener(news)
    fun onShareClick(news: News) = shareClickListener(news)
    fun onPress(news: News) = onPressListener(news)
}

class NewsDiffUtil : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.cmsId == newItem.cmsId
    }
}
