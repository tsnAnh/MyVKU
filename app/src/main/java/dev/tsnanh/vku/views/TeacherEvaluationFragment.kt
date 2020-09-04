package dev.tsnanh.vku.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.TeacherEvaluationAdapter
import dev.tsnanh.vku.databinding.FragmentTeacherEvaluationBinding
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.Resource.Loading
import dev.tsnanh.vku.domain.entities.Teacher
import dev.tsnanh.vku.viewmodels.TeacherEvaluationViewModel

@AndroidEntryPoint
class TeacherEvaluationFragment : Fragment() {
    private lateinit var teacherEvaluationAdapter: TeacherEvaluationAdapter
    private lateinit var teacherEvaluationBinding: FragmentTeacherEvaluationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
        requireActivity().onBackPressedDispatcher
            .addCallback(this) {
                findNavController().navigateUp()
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        teacherEvaluationBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_teacher_evaluation, container, false)
        return teacherEvaluationBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel by viewModels<TeacherEvaluationViewModel>()
        teacherEvaluationAdapter = TeacherEvaluationAdapter()
        teacherEvaluationBinding.rcvTeacher.apply {
            adapter = teacherEvaluationAdapter
            layoutManager =
                LinearLayoutManager(requireContext())
        }
        viewModel.teachers.observe<Resource<List<Teacher>>>(viewLifecycleOwner) { listResource ->
            when (listResource) {
                is Loading -> {
                    teacherEvaluationBinding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.text_something_went_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                    teacherEvaluationBinding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    teacherEvaluationAdapter.updateTeacher(listResource.data ?: emptyList())
                    teacherEvaluationBinding.progressBar.isVisible = false
                }
            }
        }
    }
}