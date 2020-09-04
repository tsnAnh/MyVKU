/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewholders

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.NewsClickListener
import dev.tsnanh.vku.databinding.ItemNewsBinding
import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.utils.unescapeJava

class NewsViewHolder private constructor(
    private val binding: ItemNewsBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): NewsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding =
                ItemNewsBinding.inflate(inflater, parent, false)
            return NewsViewHolder(binding)
        }
    }

    @ExperimentalStdlibApi
    fun bind(
        news: News,
        clickListener: NewsClickListener,
        position: Int
    ) {
        with(binding) {
            root.setOnCreateContextMenuListener { contextMenu, v, _ ->
                contextMenu.apply {
                    setHeaderTitle(v.context.getString(R.string.text_options))
                    add(0, position, 0, root.context.getString(R.string.text_open_in_browser))
                    add(0, position, 1, root.context.getString(R.string.text_view_attachment))
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                root.isContextClickable = false
            }
            more.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    root.showContextMenu(it.x, it.y)
                } else {
                    root.showContextMenu()
                }
            }
            this.news = news
            this.clickListener = clickListener
            date.text = news.updatedDate
            title.text = news.title!!.removeSurrounding("\"").unescapeJava()
            executePendingBindings()
        }
    }
}