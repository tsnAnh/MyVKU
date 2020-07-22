package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.viewholders.SubjectViewHolder

class TimetableAdapter(
    private val listener: TimetableClickListener
) : ListAdapter<Subject, SubjectViewHolder>(SubjectDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SubjectViewHolder.from(parent)

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

}

class SubjectDiffUtil : DiffUtil.ItemCallback<Subject>() {
    override fun areItemsTheSame(oldItem: Subject, newItem: Subject) = oldItem === newItem

    override fun areContentsTheSame(oldItem: Subject, newItem: Subject) =
        oldItem.className == newItem.className
}

class TimetableClickListener(
    val setAlarmClickListener: (Subject) -> Unit,
    val attendanceClickListener: (Subject) -> Unit
) {
    fun onSetAlarm(subject: Subject) = setAlarmClickListener(subject)
    fun onAttendance(subject: Subject) = attendanceClickListener(subject)
}