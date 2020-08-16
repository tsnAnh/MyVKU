package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.domain.entities.Notice
import dev.tsnanh.vku.viewholders.NoticeViewHolder

class NoticeAdapter(
    private var listNotices: List<Notice> = emptyList()
) : RecyclerView.Adapter<NoticeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NoticeViewHolder.from(parent)

    override fun getItemCount() = listNotices.size

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(listNotices[position])
    }

    fun updateList(list: List<Notice>) {
        this.listNotices = list
        notifyDataSetChanged()
    }

}

