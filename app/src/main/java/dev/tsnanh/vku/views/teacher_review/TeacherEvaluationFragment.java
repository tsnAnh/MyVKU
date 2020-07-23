package dev.tsnanh.vku.views.teacher_review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.transition.MaterialContainerTransform;

import dev.tsnanh.vku.R;
import dev.tsnanh.vku.adapters.TeacherEvalutionAdapter;
import dev.tsnanh.vku.databinding.FragmentTeacherEvaluationBinding;
import dev.tsnanh.vku.domain.entities.Resource;

public class TeacherEvaluationFragment extends Fragment {
    TeacherEvalutionAdapter teacherEvalutionAdapter;
    private FragmentTeacherEvaluationBinding teacherEvaluationBinding;

    public TeacherEvaluationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSharedElementEnterTransition(new MaterialContainerTransform());

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigateUp();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        teacherEvaluationBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_teacher_evaluation, container, false);

        return teacherEvaluationBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TeacherEvaluationViewModel viewModel = new ViewModelProvider(this)
                .get(TeacherEvaluationViewModel.class);

        teacherEvalutionAdapter = new TeacherEvalutionAdapter();
        teacherEvaluationBinding.rcvTeacher.setAdapter(teacherEvalutionAdapter);
        teacherEvaluationBinding.rcvTeacher
                .setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel.teachers.observe(getViewLifecycleOwner(), listResource -> {
            if (listResource instanceof Resource.Loading) {
                teacherEvaluationBinding.progressBar.setVisibility(View.VISIBLE);
            } else if (listResource instanceof Resource.Error) {
                Toast.makeText(
                        requireContext(),
                        "Không ổn rồi đại vương ạ T_T",
                        Toast.LENGTH_SHORT
                ).show();
                teacherEvaluationBinding.progressBar.setVisibility(View.VISIBLE);
            } else if (listResource instanceof Resource.Success) {
                teacherEvalutionAdapter.updateTeacher(listResource.getData());
                teacherEvaluationBinding.progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
