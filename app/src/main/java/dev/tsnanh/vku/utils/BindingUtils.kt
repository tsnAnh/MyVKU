package dev.tsnanh.vku.utils

import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import dev.tsnanh.vku.R
import dev.tsnanh.vku.domain.Forum
import dev.tsnanh.vku.domain.ForumThread

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