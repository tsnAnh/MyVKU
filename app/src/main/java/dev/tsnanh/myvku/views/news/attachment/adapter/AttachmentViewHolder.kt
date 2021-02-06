package dev.tsnanh.myvku.views.news.attachment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.tsnanh.myvku.databinding.ItemAttachmentBinding
import dev.tsnanh.myvku.utils.getDrawableType

class AttachmentViewHolder private constructor(
    private val binding: ItemAttachmentBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): AttachmentViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemAttachmentBinding.inflate(inflater, parent, false)
            return AttachmentViewHolder(binding)
        }
    }

    fun bind(attachment: String, listener: AttachmentClickListener) {
        with(binding) {
            this.attachment = attachment
            this.listener = listener
            Glide
                .with(imageView6.context)
                .load(attachment.substringAfterLast('.', "").getDrawableType())
                .into(imageView6)
            executePendingBindings()
        }
    }
}