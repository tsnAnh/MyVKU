package dev.tsnanh.myvku.views.news.pages.absence.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.tsnanh.myvku.domain.entities.Absence
import dev.tsnanh.myvku.views.news.NoticeViewHolder

class AbsenceAdapter : ListAdapter<Absence, NoticeViewHolder>(AbsenceDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NoticeViewHolder.from(parent)

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class AbsenceDiffUtil : DiffUtil.ItemCallback<Absence>() {
    override fun areItemsTheSame(oldItem: Absence, newItem: Absence): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Absence, newItem: Absence): Boolean {
        return oldItem.dateNotice == newItem.dateNotice
    }
}
