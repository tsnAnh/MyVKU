package dev.tsnanh.myvku.views.teacherevaluation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
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
class TeacherEvaluationFragment : BaseFragment<TeacherEvaluationViewModel, FragmentTeacherEvaluationBinding>() {
    override val viewModel: TeacherEvaluationViewModel by viewModels()

    private lateinit var teacherEvaluationAdapter: TeacherEvaluationAdapter

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) = FragmentTeacherEvaluationBinding.inflate(inflater, container, false)

    override fun FragmentTeacherEvaluationBinding.initViews() {
        teacherEvaluationAdapter = TeacherEvaluationAdapter()
        rcvTeacher.apply {
            adapter = teacherEvaluationAdapter
            layoutManager =
                LinearLayoutManager(requireContext())
        }
    }

    override suspend fun TeacherEvaluationViewModel.observeData() {
        teachers.collect { listResource ->
            when (listResource) {
                is Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is State.Error -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.text_something_went_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar.visibility = View.VISIBLE
                }
                is State.Success -> {
                    listResource.data?.let { it1 -> teacherEvaluationAdapter.updateTeacher(it1) }
                    binding.progressBar.isVisible = false
                }
            }
        }
    }
}
