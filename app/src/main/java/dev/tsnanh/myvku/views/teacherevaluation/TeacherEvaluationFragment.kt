package dev.tsnanh.myvku.views.teacherevaluation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.base.BaseFragment
import dev.tsnanh.myvku.databinding.FragmentTeacherEvaluationBinding
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.entities.State.Loading
import dev.tsnanh.myvku.views.teacherevaluation.adapter.TeacherEvaluationAdapter
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TeacherEvaluationFragment : BaseFragment() {
    private lateinit var teacherEvaluationAdapter: TeacherEvaluationAdapter
    private lateinit var teacherEvaluationBinding: FragmentTeacherEvaluationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        teacherEvaluationBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_teacher_evaluation, container, false)
        return teacherEvaluationBinding.root
    }

    override val viewModel: TeacherEvaluationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teacherEvaluationAdapter = TeacherEvaluationAdapter()
        teacherEvaluationBinding.rcvTeacher.apply {
            adapter = teacherEvaluationAdapter
            layoutManager =
                LinearLayoutManager(requireContext())
        }
    }

    override fun setupView() {
    }

    override fun bindView() {
        lifecycleScope.launchWhenStarted {
            viewModel.teachers.collect { listResource ->
                when (listResource) {
                    is Loading -> {
                        teacherEvaluationBinding.progressBar.visibility = View.VISIBLE
                    }
                    is State.Error -> {
                        Toast.makeText(
                            requireContext(),
                            requireContext().getString(R.string.text_something_went_wrong),
                            Toast.LENGTH_SHORT
                        ).show()
                        teacherEvaluationBinding.progressBar.visibility = View.VISIBLE
                    }
                    is State.Success -> {
                        listResource.data?.let { it1 -> teacherEvaluationAdapter.updateTeacher(it1) }
                        teacherEvaluationBinding.progressBar.isVisible = false
                    }
                }
            }
        }
    }
}