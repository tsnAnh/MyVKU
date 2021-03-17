/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.myvku.views.news.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.databinding.ItemNewsBinding
import dev.tsnanh.myvku.domain.entities.News
import dev.tsnanh.myvku.utils.unescapeJava

class NewsViewHolder private constructor(
    private val binding: ItemNewsBinding,
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): NewsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding =
                ItemNewsBinding.inflate(inflater, parent, false)
            return NewsViewHolder(binding)
        }
    }

    fun bind(
        news: News,
        listener: NewsClickListener,
        position: Int,
    ) {
        with(binding) {
            with(root) {
                setOnCreateContextMenuListener { contextMenu, v, _ ->
                    contextMenu.apply {
                        setHeaderTitle(v.context.getString(R.string.text_options))
                        add(0, position, 0, root.context.getString(R.string.text_open_in_browser))
                        add(0, position, 1, root.context.getString(R.string.text_view_attachment))
                    }
                }
                setOnLongClickListener { listener.onPress(news) }
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
            this.clickListener = listener
            date.text = news.updatedDate
            title.text = news.title?.removeSurrounding("\"")?.unescapeJava()?.replace("\\", "")
            executePendingBindings()
        }
    }
}
