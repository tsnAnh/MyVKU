package dev.tsnanh.myvku.views.news.pages.makeup.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.tsnanh.myvku.domain.entities.MakeupClass
import dev.tsnanh.myvku.views.news.NoticeViewHolder

class MakeupClassAdapter : ListAdapter<MakeupClass, NoticeViewHolder>(MakeupClassDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NoticeViewHolder.from(parent)

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MakeupClassDiffUtil : DiffUtil.ItemCallback<MakeupClass>() {
    override fun areItemsTheSame(oldItem: MakeupClass, newItem: MakeupClass): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: MakeupClass, newItem: MakeupClass): Boolean {
        return oldItem.dateMakeUp == newItem.dateMakeUp
    }
}
