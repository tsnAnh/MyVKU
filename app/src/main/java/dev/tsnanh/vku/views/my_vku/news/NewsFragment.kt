/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views.my_vku.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.transition.MaterialFadeThrough
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.NewsAdapter
import dev.tsnanh.vku.adapters.NewsClickListener
import dev.tsnanh.vku.databinding.FragmentNewsBinding
import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.CustomTabHelper
import dev.tsnanh.vku.utils.showSnackbarWithAction
import dev.tsnanh.vku.viewmodels.my_vku.NewsViewModel
import kotlinx.coroutines.launch

class NewsFragment : Fragment() {

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var binding: FragmentNewsBinding
    private val customTabHelper = CustomTabHelper()
    private val preferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_news, container, false)

        setHasOptionsMenu(true)
        viewModel.refreshNews()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewModel = viewModel

        val adapter = NewsAdapter(
            NewsClickListener(
                viewClickListener = viewClick,
                shareClickListener = shareClick
            )
        )
        binding.listNews.apply {
            setHasFixedSize(false)
            isNestedScrollingEnabled = true
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.news.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.listNews.adapter = adapter
                adapter.submitList(it)
                binding.swipeToRefresh.isRefreshing = false
            }
        })

        viewModel.navigateToView.observe(viewLifecycleOwner, Observer {
            it?.let {
                launchNews(it)
            }
        })

        viewModel.filterData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(viewModel.listNewsLocal?.filter { news ->
                    news.category == it
                })
            }
        })

        viewModel.shareAction.observe(viewLifecycleOwner, Observer {
            it?.let {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${it.title}\n${addDomain(it.url)}")
                    type = "text/plain"
                }
                startActivity(intent)
                viewModel.onShareButtonClicked()
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.apply {
                    swipeToRefresh.isRefreshing = false
                    chipsFilterNews.clearCheck()
                }
                showSnackbarWithAction(
                    requireView(),
                    it,
                    requireContext().getString(R.string.text_hide)
                )
                viewModel.onErrorDisplayed()
            }
        })

        binding.swipeToRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                binding.swipeToRefresh.isRefreshing = true
                for (chip in binding.chipsFilterNews.children) {
                    (chip as Chip).isChecked = false
                }
                viewModel.refreshNews()
            }
        }
    }

    private val viewClick: (News) -> Unit = {
        viewModel.onNavigateToView(it)
    }

    private val shareClick: (News) -> Unit = {
        viewModel.onShareButtonClick(it)
    }

    private val addDomain: (String) -> String = {
        "${Constants.DAO_TAO_URL}$it"
    }

    private fun launchNews(news: News) {
        val url = addDomain(news.url)
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.primaryColor))
        builder.addDefaultShareMenuItem()
        builder.setShowTitle(true)
        builder.setStartAnimations(
            requireActivity(),
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        builder.setExitAnimations(
            requireActivity(),
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        builder.setColorScheme(
            when (preferences.getString(
                getString(R.string.night_mode_key),
                Constants.MODE_SYSTEM
            )) {
                Constants.MODE_DARK -> CustomTabsIntent.COLOR_SCHEME_DARK
                Constants.MODE_LIGHT -> CustomTabsIntent.COLOR_SCHEME_LIGHT
                else -> CustomTabsIntent.COLOR_SCHEME_SYSTEM
            }
        )
        val customTabsIntent = builder.build()

        // check is chrome available
        val packageName = customTabHelper.getPackageNameToUse(requireActivity(), url)

        if (packageName == null) {
            findNavController().navigate(
                NewsFragmentDirections.actionNavigationNewsToActivityNews(
                    news.url, news.title
                )
            )
        } else {
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(requireActivity(), Uri.parse(url))
        }
        viewModel.onNavigatedToView()
    }
}
