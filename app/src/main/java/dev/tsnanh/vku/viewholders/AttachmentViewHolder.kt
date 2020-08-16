package dev.tsnanh.vku.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.tsnanh.vku.adapters.AttachmentClickListener
import dev.tsnanh.vku.databinding.ItemAttachmentBinding
import dev.tsnanh.vku.utils.getTypeDrawable

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
                .load(attachment.substringAfterLast('.', "").getTypeDrawable())
                .into(imageView6)
            executePendingBindings()
        }
    }
}