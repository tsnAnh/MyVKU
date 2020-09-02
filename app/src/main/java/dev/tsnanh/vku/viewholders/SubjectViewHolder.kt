package dev.tsnanh.vku.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.adapters.TimetableClickListener
import dev.tsnanh.vku.databinding.ItemSubjectBinding
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.checkSubjectHasValidAlarm
import dev.tsnanh.vku.utils.checkValidWeek

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
                subject.checkSubjectHasValidAlarm() && subject.week.checkValidWeek()
            if (subject.room !in Constants.ROOMS.keys) comeInClass.isEnabled = false
            executePendingBindings()
        }
    }
}