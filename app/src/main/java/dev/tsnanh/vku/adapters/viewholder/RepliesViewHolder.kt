/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.databinding.ItemFirstReplyBinding
import dev.tsnanh.vku.databinding.ItemNormalReplyBinding
import dev.tsnanh.vku.domain.Post
import dev.tsnanh.vku.utils.convertTimestampToDateString

sealed class RepliesViewHolder<T : ViewDataBinding>(
    binding: T
) : RecyclerView.ViewHolder(binding.root) {

    class FirstReplyViewHolder private constructor(
        private val binding: ItemFirstReplyBinding
    ) : RepliesViewHolder<ItemFirstReplyBinding>(binding) {
        companion object {
            fun from(parent: ViewGroup): FirstReplyViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemFirstReplyBinding.inflate(inflater, parent, false)
                return FirstReplyViewHolder(binding)
            }
        }

        fun bind(post: Post?) {
            post?.let {
                binding.post = it
                binding.timeCreated = convertTimestampToDateString(it.createdAt)
            }
            binding.executePendingBindings()
        }
    }

    class NormalReplyViewHolder private constructor(
        private val binding: ItemNormalReplyBinding
    ) : RepliesViewHolder<ItemNormalReplyBinding>(binding) {
        companion object {
            fun from(parent: ViewGroup): NormalReplyViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemNormalReplyBinding.inflate(inflater, parent, false)
                return NormalReplyViewHolder(binding)
            }
        }

        fun bind(post: Post?) {
            binding.post = post
            binding.executePendingBindings()
        }
    }
}