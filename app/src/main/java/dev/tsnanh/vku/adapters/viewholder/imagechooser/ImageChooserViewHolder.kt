package dev.tsnanh.vku.adapters.viewholder.imagechooser

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.adapters.ImageChooserClickListener
import dev.tsnanh.vku.databinding.ItemImageChooserBinding

class ImageChooserViewHolder private constructor(
    private val binding: ItemImageChooserBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ImageChooserViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemImageChooserBinding
                .inflate(inflater, parent, false)

            return ImageChooserViewHolder(
                binding
            )
        }
    }

    fun bind(
        uri: Uri,
        listener: ImageChooserClickListener,
        position: Int
    ) {
        binding.uri = uri
        binding.clickListener = listener
        binding.position = position
        binding.executePendingBindings()
    }
}