package dev.tsnanh.vku.views.my_vku.teacher_evaluation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import dev.tsnanh.vku.R;
import dev.tsnanh.vku.adapters.TeacherEvalutionAdapter;
import dev.tsnanh.vku.databinding.FragmentTeacherEvaluationBinding;
import dev.tsnanh.vku.domain.entities.Resource;
import dev.tsnanh.vku.domain.entities.Teacher;

public class TeacherEvaluationFragment extends Fragment {
    TeacherEvalutionAdapter teacherEvalutionAdapter;
    private TeacherEvaluationViewModel viewModel;
    private FragmentTeacherEvaluationBinding teacherEvaluationBinding;

    public TeacherEvaluationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        teacherEvaluationBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_teacher_evaluation, container, false);

        return teacherEvaluationBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(TeacherEvaluationViewModel.class);

        teacherEvalutionAdapter = new TeacherEvalutionAdapter();
        teacherEvaluationBinding.rcvTeacher.setAdapter(teacherEvalutionAdapter);
        teacherEvaluationBinding.rcvTeacher.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel.teachers.observe(getViewLifecycleOwner(), new Observer<Resource<List<Teacher>>>() {
            @Override
            public void onChanged(Resource<List<Teacher>> listResource) {
                if (listResource instanceof Resource.Loading) {
                    teacherEvaluationBinding.progressBar.setVisibility(View.VISIBLE);
                } else if (listResource instanceof Resource.Error) {
                    Toast.makeText(requireContext(), "Không ổn rồi đại vương ạ T_T", Toast.LENGTH_SHORT).show();
                } else if (listResource instanceof Resource.Success) {
                    teacherEvaluationBinding.progressBar.setVisibility(View.INVISIBLE);
                    teacherEvalutionAdapter.updateTeacher(listResource.getData());
                }
            }
        });
    }
}
