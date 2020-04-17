/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.utils

import android.app.Activity
import android.net.Uri
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.auth.FirebaseUser
import dev.tsnanh.vku.R
import dev.tsnanh.vku.domain.Forum
import dev.tsnanh.vku.domain.ForumThread
import dev.tsnanh.vku.domain.Post
import timber.log.Timber

@BindingAdapter("categoryNews")
fun TextView.setCategory(category: String?) {
    category?.let {
        text = when (category) {
            "thong-bao-chung" -> this.context.getString(R.string.text_thong_bao_chung)
            "thong-bao-ctsv" -> this.context.getString(R.string.text_thong_bao_ctsv)
            "thong-bao-khtc" -> this.context.getString(R.string.text_thong_bao_khtc)
            else -> this.context.getString(R.string.text_uncategorized)
        }
    }
}

@BindingAdapter("userAvatar")
fun ImageView.setAvatar(user: FirebaseUser?) {
    user?.let {
        Glide
            .with(this.context)
            .load(user.photoUrl)
            .circleCrop()
            .into(this)
    }
}

@BindingAdapter("forumImage")
fun ImageView.setForumImage(forum: Forum?) {
    forum?.let {
        Glide
            .with(this.context)
            .load(forum.image)
            .into(this)
    }
}

@BindingAdapter("imageChooser")
fun ImageView.setChooserImage(uri: Uri?) {
    uri?.let {
        Glide
            .with(this.context)
            .load(it)
            .into(this)
    }
}

@BindingAdapter("pickerHasImage")
fun Button.setButtonVisible(visibility: Boolean) {
    this.visibility = when (visibility) {
        true -> View.GONE
        false -> View.VISIBLE
    }
}

@BindingAdapter("hasImage")
fun RecyclerView.setHasImage(visibility: Boolean) {
    this.visibility = when (visibility) {
        true -> View.VISIBLE
        false -> View.INVISIBLE
    }
}

@BindingAdapter("itemThreadAvatar")
fun ImageView.setItemThreadAvatar(thread: ForumThread) {
    Glide
        .with(this.context)
        .load(thread.userAvatar)
        .centerCrop()
        .circleCrop()
        .into(this)
}

@BindingAdapter("postAvatar")
fun ImageView.setPostAvatar(post: Post?) {
    post?.let {
        Glide
            .with(this.context)
            .load(post.userAvatar)
            .centerCrop()
            .circleCrop()
            .into(this)
    }
}

@BindingAdapter("postImages")
fun FlexboxLayout.setImages(post: Post?) {
    post?.let {
        val imageCount = it.images.size
        it.images.forEach { image ->
            val imageView = AppCompatImageView(this.context)
            if (imageCount > 2) imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            val metrics = DisplayMetrics()
            (this.context as Activity).windowManager
                .defaultDisplay
                .getMetrics(metrics)
            val params = FlexboxLayout.LayoutParams(
                when (imageCount) {
                    1, 2 -> FlexboxLayout.LayoutParams.MATCH_PARENT
                    else -> metrics.widthPixels / 3
                },
                when (imageCount) {
                    1, 2 -> FlexboxLayout.LayoutParams.WRAP_CONTENT
                    else -> metrics.widthPixels / 3
                }
            )
//            params.flexBasisPercent = 30F
            params.order = 1
            params.flexShrink = 1F
            imageView.layoutParams = params
            Glide
                .with(imageView)
                .load("http://34.87.13.195:3000/$image")
                .into(imageView)
            Timber.d("http://34.87.13.195:3000/$image")
            this.addView(imageView, params)
        }
    }
}