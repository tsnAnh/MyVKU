package dev.tsnanh.vku.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import dev.tsnanh.vku.R;
import dev.tsnanh.vku.databinding.ItemTeacherEvaluationBinding;
import dev.tsnanh.vku.domain.entities.Teacher;
import dev.tsnanh.vku.viewholders.TeacherEvaluationViewHolder;

public class TeacherEvalutionAdapter extends RecyclerView.Adapter<TeacherEvaluationViewHolder> {
    private List<Teacher> teachers;
    private ItemTeacherEvaluationBinding teacherEvaluationBinding;
    private Teacher teacher;

    public TeacherEvalutionAdapter() {
        teachers = Collections.emptyList();
    }

    @NonNull
    @Override
    public TeacherEvaluationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        teacherEvaluationBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.item_teacher_evaluation, parent, false);

        return new TeacherEvaluationViewHolder(teacherEvaluationBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherEvaluationViewHolder holder, int position) {
        teacher = teachers.get(position);
        holder.bind(teacher);
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public void updateTeacher(List<Teacher> teachers) {
        this.teachers = teachers;
        notifyDataSetChanged();
    }
}
