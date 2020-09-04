package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.tsnanh.vku.domain.entities.MakeUpClass
import dev.tsnanh.vku.viewholders.NoticeViewHolder

class MakeupClassAdapter : ListAdapter<MakeUpClass, NoticeViewHolder>(MakeupClassDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NoticeViewHolder.from(parent)

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MakeupClassDiffUtil : DiffUtil.ItemCallback<MakeUpClass>() {
    override fun areItemsTheSame(oldItem: MakeUpClass, newItem: MakeUpClass): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: MakeUpClass, newItem: MakeUpClass): Boolean {
        return oldItem.dateMakeUp == newItem.dateMakeUp
    }
}