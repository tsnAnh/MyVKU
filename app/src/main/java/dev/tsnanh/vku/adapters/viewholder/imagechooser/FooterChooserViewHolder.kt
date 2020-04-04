package dev.tsnanh.vku.adapters.viewholder.imagechooser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.adapters.ImageChooserClickListener
import dev.tsnanh.vku.databinding.ItemChooserFooterBinding

class FooterChooserViewHolder private constructor(
    private val binding: ItemChooserFooterBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): FooterChooserViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding =
                ItemChooserFooterBinding.inflate(inflater, parent, false)
            return FooterChooserViewHolder(binding)
        }
    }

    fun bind(listener: ImageChooserClickListener) {
        binding.listener = listener
        binding.executePendingBindings()
    }
}