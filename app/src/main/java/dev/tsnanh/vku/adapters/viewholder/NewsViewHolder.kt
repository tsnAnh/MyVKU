package dev.tsnanh.vku.adapters.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.adapters.NewsClickListener
import dev.tsnanh.vku.databinding.ItemNewsBinding
import dev.tsnanh.vku.domain.News

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

    fun bind(
        news: News,
        clickListener: NewsClickListener
    ) {
        binding.news = news
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }
}