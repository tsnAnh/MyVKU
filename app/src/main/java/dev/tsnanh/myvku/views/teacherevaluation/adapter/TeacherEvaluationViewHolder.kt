package dev.tsnanh.myvku.views.teacherevaluation.adapter

import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.myvku.databinding.ItemTeacherEvaluationBinding
import dev.tsnanh.myvku.domain.entities.Teacher

class TeacherEvaluationViewHolder(private val binding: ItemTeacherEvaluationBinding) :
    RecyclerView.ViewHolder(
        binding.root
    ) {
    fun bind(teacher: Teacher?) {
        binding.teacher = teacher
        binding.executePendingBindings()
    }
}