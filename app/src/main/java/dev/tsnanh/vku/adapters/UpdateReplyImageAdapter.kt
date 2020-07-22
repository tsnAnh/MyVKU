package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.viewholders.imagechooser.UpdateReplyImageChooserViewHolder

class UpdateReplyImageAdapter(
    var listImages: List<String>,
    private val listener: ImageChooserClickListener
) : RecyclerView.Adapter<UpdateReplyImageChooserViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpdateReplyImageChooserViewHolder {
        return UpdateReplyImageChooserViewHolder.from(parent)
    }

    override fun getItemCount() = listImages.size

    override fun onBindViewHolder(holder: UpdateReplyImageChooserViewHolder, position: Int) {
        holder.bind(listImages[position], listener)
    }

    fun updateList(images: List<String>) {
        this.listImages = images
        notifyDataSetChanged()
    }
}