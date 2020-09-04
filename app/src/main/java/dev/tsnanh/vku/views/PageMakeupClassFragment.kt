package dev.tsnanh.vku.views

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.MakeupClassAdapter
import dev.tsnanh.vku.databinding.FragmentPageMakeupClassBinding
import dev.tsnanh.vku.domain.entities.MakeUpClass
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.isInternetAvailableApi23
import dev.tsnanh.vku.utils.showSnackbarWithAction
import dev.tsnanh.vku.viewmodels.MainViewModel
import dev.tsnanh.vku.viewmodels.PageMakeupClassViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class PageMakeupClassFragment : Fragment() {
    companion object {
        fun newInstance() = PageMakeupClassFragment()
    }

    private lateinit var binding: FragmentPageMakeupClassBinding
    private lateinit var adapterMakeupClass: MakeupClassAdapter
    private val viewModel: PageMakeupClassViewModel by viewModels()
    private val mainViewModel by activityViewModels<MainViewModel>()

    private var isNetworkAvailable = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_page_makeup_class, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        adapterMakeupClass = MakeupClassAdapter()

        binding.listMakeupClass.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = adapterMakeupClass
        }

        mainViewModel.connectivityLiveData.observe(viewLifecycleOwner) { available ->
            isNetworkAvailable = available
        }

        viewModel.makeUpClass
            .observe<Resource<List<MakeUpClass>>>(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Error -> {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            if (!requireContext().isInternetAvailableApi23()) {
                                showSnackbarWithAction(requireView(),
                                    requireContext().getString(R.string.text_no_internet_connection))
                            }
                        } else {
                            if (!isNetworkAvailable) {
                                showSnackbarWithAction(requireView(),
                                    requireContext().getString(R.string.text_no_internet_connection))
                            }
                        }
                    }
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        result.data?.let { adapterMakeupClass.submitList(it) }
                    }
                }
            }
    }
}