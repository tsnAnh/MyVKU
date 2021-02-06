package dev.tsnanh.myvku.views.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.myvku.databinding.ItemSubjectBinding
import dev.tsnanh.myvku.domain.entities.Subject
import dev.tsnanh.myvku.utils.Constants
import dev.tsnanh.myvku.utils.hasValidAlarm
import dev.tsnanh.myvku.utils.isValidWeek

class SubjectViewHolder(
    private val binding: ItemSubjectBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): SubjectViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemSubjectBinding.inflate(inflater, parent, false)

            return SubjectViewHolder(binding)
        }
    }

    fun bind(
        subject: Subject,
        listener: TimetableClickListener
    ) {
        binding.apply {
            this.subject = subject
            this.listener = listener
            setAlarm.isEnabled =
                subject.hasValidAlarm() && subject.week.isValidWeek
            if (subject.room !in Constants.ROOMS.keys) comeInClass.isEnabled = false
            executePendingBindings()
        }
    }
}