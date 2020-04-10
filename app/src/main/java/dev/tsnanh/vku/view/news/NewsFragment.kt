package dev.tsnanh.vku.view.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.NewsAdapter
import dev.tsnanh.vku.adapters.NewsClickListener
import dev.tsnanh.vku.databinding.FragmentNewsBinding
import dev.tsnanh.vku.domain.News
import dev.tsnanh.vku.utils.CustomTabHelper

class NewsFragment : Fragment() {

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var binding: FragmentNewsBinding
    private val customTabHelper = CustomTabHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough.create(requireContext())
        exitTransition = MaterialFadeThrough.create(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_news, container, false)

        setHasOptionsMenu(true)

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
        val filterAdapter = NewsAdapter(
            NewsClickListener(
                viewClickListener = viewClick,
                shareClickListener = shareClick
            )
        )
        binding.listNews.setHasFixedSize(false)
        binding.listNews.isNestedScrollingEnabled = true
        binding.listNews.layoutManager = LinearLayoutManager(requireContext())

        viewModel.listNews.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.listNews.adapter = adapter
                adapter.submitList(it)
            }
        })

        viewModel.navigateToView.observe(viewLifecycleOwner, Observer {
            it?.let {
                launchNews(it)
            }
        })

        viewModel.isFiltered.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.listNews.adapter = filterAdapter
            } else {
                binding.listNews.adapter = adapter
            }
        })

        viewModel.filterData.observe(viewLifecycleOwner, Observer {
            it?.let {
                filterAdapter.submitList(adapter.currentList.filter { news ->
                    news.category == it
                })
            }
        })

        viewModel.shareAction.observe(viewLifecycleOwner, Observer {
            it?.let {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, "${it.title}\n${addDomain(it.url)}")
                intent.type = "text/plain"
                startActivity(intent)
                viewModel.onShareButtonClicked()
            }
        })

        viewModel.refreshNews.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (!it) binding.swipeToRefresh.isRefreshing = it
            }
        })

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.refreshNews()
        }
    }

    private val viewClick: (News) -> Unit = {
        viewModel.onNavigateToView(it)
    }

    private val shareClick: (News) -> Unit = {
        viewModel.onShareButtonClick(it)
    }

    private val addDomain: (String) -> String = {
        "http://daotao.sict.udn.vn$it"
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
