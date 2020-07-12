package dev.tsnanh.vku.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.tsnanh.vku.databinding.ItemTeacherEvaluationBinding;
import dev.tsnanh.vku.domain.entities.Teacher;

public class TeacherEvaluationViewHolder extends RecyclerView.ViewHolder {
    private ItemTeacherEvaluationBinding binding;

    public TeacherEvaluationViewHolder(@NonNull ItemTeacherEvaluationBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Teacher teacher) {
        binding.setTeacher(teacher);
        binding.hasPendingBindings();
    }

}
