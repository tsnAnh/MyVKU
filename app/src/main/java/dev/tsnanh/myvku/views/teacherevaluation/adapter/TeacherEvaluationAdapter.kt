package dev.tsnanh.myvku.views.teacherevaluation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.myvku.databinding.ItemTeacherEvaluationBinding
import dev.tsnanh.myvku.domain.entities.Teacher

class TeacherEvaluationAdapter(
    private var teachers: List<Teacher> = emptyList(),
) : RecyclerView.Adapter<TeacherEvaluationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherEvaluationViewHolder {
        val teacherEvaluationBinding = ItemTeacherEvaluationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TeacherEvaluationViewHolder(teacherEvaluationBinding)
    }

    override fun onBindViewHolder(holder: TeacherEvaluationViewHolder, position: Int) {
        holder.bind(teachers[position])
    }

    override fun getItemCount() = teachers.size

    fun updateTeacher(teachers: List<Teacher>) {
        this.teachers = teachers
        notifyDataSetChanged()
    }
}
