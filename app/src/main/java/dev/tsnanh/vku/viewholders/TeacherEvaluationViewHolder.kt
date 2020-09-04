package dev.tsnanh.vku.viewholders

import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.databinding.ItemTeacherEvaluationBinding
import dev.tsnanh.vku.domain.entities.Teacher

class TeacherEvaluationViewHolder(private val binding: ItemTeacherEvaluationBinding) :
    RecyclerView.ViewHolder(
        binding.root
    ) {
    fun bind(teacher: Teacher?) {
        binding.teacher = teacher
        binding.executePendingBindings()
    }
}