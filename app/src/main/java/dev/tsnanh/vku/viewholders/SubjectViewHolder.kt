package dev.tsnanh.vku.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.adapters.TimetableClickListener
import dev.tsnanh.vku.databinding.ItemSubjectBinding
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.utils.checkSubjectHasValidAlarm

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
        binding.subject = subject
        binding.listener = listener
        binding.setAlarm.isEnabled = subject.checkSubjectHasValidAlarm()
        binding.executePendingBindings()
    }
}