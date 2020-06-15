package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.viewholders.SubjectViewHolder

class TimetableAdapter(
    private var subjects: List<Subject> = emptyList(),
    private val listener: TimetableClickListener
) : RecyclerView.Adapter<SubjectViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SubjectViewHolder.from(parent)

    override fun getItemCount() = subjects.size

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(subjects[position], listener)
    }

    fun updateSubjects(subjects: List<Subject>) {
        this.subjects = subjects
        notifyDataSetChanged()
    }
}

class TimetableClickListener(
    val setAlarmClickListener: (Subject) -> Unit,
    val attendanceClickListener: (Subject) -> Unit
) {
    fun onSetAlarm(subject: Subject) = setAlarmClickListener(subject)
    fun onAttendance(subject: Subject) = attendanceClickListener(subject)
}