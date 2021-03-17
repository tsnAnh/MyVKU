package dev.tsnanh.myvku.extension

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop

fun ImageView.load(url: String, transformation: Transformation<Bitmap> = CenterCrop(), circleCrop: Boolean = false) {
    val builder = Glide.with(this).load(url)
    if (circleCrop) {
        builder.circleCrop()
    } else {
        builder
    }.transform(transformation).into(this)
}
fun ImageView.load(uri: Uri, transformation: Transformation<Bitmap> = CenterCrop(), circleCrop: Boolean = false) {
    val builder = Glide.with(this).load(uri)
    if (circleCrop) {
        builder.circleCrop()
    } else {
        builder
    }.transform(transformation).into(this)
}
