/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.adapters.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.databinding.ItemNormalReplyBinding
import dev.tsnanh.vku.databinding.ItemPostBinding
import dev.tsnanh.vku.domain.Post
import dev.tsnanh.vku.utils.convertTimestampToDateString
import timber.log.Timber

sealed class RepliesViewHolder<T : ViewDataBinding>(
    binding: T
) : RecyclerView.ViewHolder(binding.root) {

    class PostViewHolder private constructor(
        private val binding: ItemPostBinding
    ) : RepliesViewHolder<ItemPostBinding>(binding) {
        companion object {
            fun from(parent: ViewGroup): PostViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemPostBinding.inflate(inflater, parent, false)
                return PostViewHolder(binding)
            }
        }

        fun bind(post: Post?) {
            post?.let {
                binding.post = it
                binding.timeCreated = it.createdAt.convertTimestampToDateString()
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
            Timber.d(post.toString())
            post?.let {
                binding.post = it
                binding.timeCreated = it.createdAt.convertTimestampToDateString()
                binding.quotedPost = it.quotedPost
                Timber.d("${it.quotedPost}")
                binding.quotedTimeCreated =
                    it.quotedPost?.createdAt?.convertTimestampToDateString()
                if (it.quotedPost != null) {
                    binding.materialCardView.visibility = View.VISIBLE
                } else {
                    binding.materialCardView.visibility = View.GONE
                }
            }
            binding.executePendingBindings()
        }
    }
}