package dev.tsnanh.myvku.views.news.attachment.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject

class AttachmentAdapter @Inject constructor(
    private val listAttachments: List<String>,
    private val listener: AttachmentClickListener
) : RecyclerView.Adapter<AttachmentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        return AttachmentViewHolder.from(parent)
    }

    override fun getItemCount() = listAttachments.size

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        holder.bind(listAttachments[position], listener)
    }
}

class AttachmentClickListener(val listener: (String) -> Unit) {
    fun onClick(attachment: String) = listener(attachment)
}
