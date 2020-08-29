package dev.tsnanh.vku.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.NoticeAdapter
import dev.tsnanh.vku.databinding.FragmentPageAbsenceBinding
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.showSnackbarWithAction
import dev.tsnanh.vku.viewmodels.PageAbsenceViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class PageAbsenceFragment : Fragment() {
    companion object {
        fun newInstance() = PageAbsenceFragment()
    }

    private lateinit var binding: FragmentPageAbsenceBinding
    private val viewModel: PageAbsenceViewModel by viewModels()
    private lateinit var adapterAbsence: NoticeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_page_absence, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        adapterAbsence = NoticeAdapter(emptyList())

        binding.listAbsences.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = adapterAbsence
        }

        viewModel.absences
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Error -> showSnackbarWithAction(requireView(),
                        result.message ?: "Error")
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        result.data?.let { adapterAbsence.updateList(it) }
                    }
                }
            }
    }
}