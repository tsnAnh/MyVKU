package dev.tsnanh.vku.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import dev.tsnanh.vku.R
import dev.tsnanh.vku.domain.Forum

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