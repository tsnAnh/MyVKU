package dev.tsnanh.vku.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.MakeupClassAdapter
import dev.tsnanh.vku.databinding.FragmentPageNewsBinding
import dev.tsnanh.vku.domain.entities.MakeUpClass
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.showSnackbar
import dev.tsnanh.vku.viewmodels.MainViewModel
import dev.tsnanh.vku.viewmodels.PageMakeupClassViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class PageMakeupClassFragment : Fragment() {
    companion object {
        fun newInstance() = PageMakeupClassFragment()
    }

    private lateinit var binding: FragmentPageNewsBinding
    private lateinit var adapterMakeupClass: MakeupClassAdapter
    private val viewModel: PageMakeupClassViewModel by viewModels()
    private val activityViewModel by activityViewModels<MainViewModel>()

    private var isNetworkAvailable = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_page_news, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        adapterMakeupClass = MakeupClassAdapter()

        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = adapterMakeupClass
        }

        activityViewModel.connectivityLiveData.observe<Boolean>(viewLifecycleOwner) { available ->
            if (available) {
                isNetworkAvailable = true
                viewModel.refresh()
            } else {
                isNetworkAvailable = false
                showLayout(requireContext().getString(R.string.text_no_internet_connection),
                    R.drawable.ic_baseline_wifi_off_24)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { t ->
            t?.let {
                when (t) {
                    is ConnectException -> showLayout(requireContext().getString(R.string.text_no_internet_connection),
                        R.drawable.ic_baseline_wifi_off_24)
                    is SocketException -> {
                        showLayout(requireContext().getString(R.string.text_no_internet_connection),
                            R.drawable.ic_baseline_wifi_off_24)
                        showSnackbar(requireView(), "Da ngat ket noi")
                    }
                    is SocketTimeoutException -> {
                        showLayout(requireContext().getString(R.string.err_msg_request_timeout),
                            R.drawable.sad)
                        showSnackbar(requireView(),
                            requireContext().getString(R.string.err_msg_request_timeout))
                    }
                    is UnknownHostException -> {
                        binding.progressBar.isVisible = false
                        showLayout("Unknown host",
                            R.drawable.sad)
                        showSnackbar(view, "Unknown host")
                    }
                    else -> Timber.e(t)
                }
                binding.swipeToRefresh.isRefreshing = false
                viewModel.clearError()
            }
        }

        viewModel.makeUpClass
            .observe<Resource<List<MakeUpClass>>>(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Error -> {
                        viewModel.onError(result.throwable)
                    }
                    is Resource.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is Resource.Success -> {
                        with(binding) {
                            swipeToRefresh.isRefreshing = false
                            progressBar.isVisible = false
                        }
                        val makeupClasses = result.data
                        if (makeupClasses != null && makeupClasses.isNotEmpty()) {
                            adapterMakeupClass.submitList(result.data)
                        } else {
                            showLayout(requireContext().getString(R.string.text_no_absences_here),
                                R.drawable.empty)
                        }
                    }
                }
            }
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun showLayout(messageString: String, drawable: Int) {
        with(binding.layoutNoItem) {
            message.text = messageString
            image.setImageResource(drawable)
            root.isVisible = true
        }
    }
}