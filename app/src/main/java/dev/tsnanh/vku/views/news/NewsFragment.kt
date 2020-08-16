/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views.news

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialFadeThrough
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.*
import dev.tsnanh.vku.databinding.AttachmentDialogLayoutBinding
import dev.tsnanh.vku.databinding.FragmentNewsBinding
import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.receivers.AttachmentReceiver
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.CustomTabHelper
import dev.tsnanh.vku.utils.SecretConstants
import dev.tsnanh.vku.utils.showSnackbarWithAction
import dev.tsnanh.vku.viewmodels.NewsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.URLConnection

class NewsFragment : Fragment() {

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var binding: FragmentNewsBinding
    private val customTabHelper = CustomTabHelper()
    private lateinit var adapterNews: NewsAdapter
    private lateinit var adapterAbsence: NoticeAdapter
    private lateinit var adapterMakeUpClass: NoticeAdapter
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
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewModel = viewModel

        adapterNews = NewsAdapter(
            NewsClickListener(
                viewClickListener = viewClick,
                shareClickListener = shareClick
            )
        )
        adapterAbsence = NoticeAdapter(emptyList())
        adapterMakeUpClass = NoticeAdapter(emptyList())
        binding.chipsFilterNews.apply {
            check(binding.newsChip.id)
            setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    binding.absence.id -> {
                        isShowProgressBar(false)
                        binding.listNews.isVisible = false
                        binding.listNotices.apply {
                            adapter = adapterAbsence
                            isVisible = true
                        }
                    }
                    binding.makeup.id -> {
                        isShowProgressBar(false)
                        binding.listNews.isVisible = false
                        binding.listNotices.apply {
                            adapter = adapterMakeUpClass
                            isVisible = true
                        }
                    }
                    binding.confession.id -> TODO("confessionAdapter")
                    else -> {
                        binding.apply {
                            listNotices.isVisible = false
                            listNews.isVisible = true
                        }
                    }
                }
            }
        }
        binding.listNews.configureList()
        binding.listNotices.configureList()

        lifecycleScope.launch {
            viewModel.absences("")
                .flowOn(Dispatchers.IO)
                .catch { updateUIError(it.message) }
                .collectLatest {
                    adapterAbsence.updateList(it)
                    isShowProgressBar(false)
                }

            viewModel
                .makeUpClass("")
                .catch { updateUIError(it.message) }
                .collectLatest {
                    adapterMakeUpClass.updateList(it)
                    isShowProgressBar(false)
                }

            viewModel.news
                .onStart {
                    isShowProgressBar(true)
                }
                .catch { updateUIError(it.message) }
                .collectLatest {
                    adapterNews.submitList(it)
                    isShowProgressBar(false)
                }
        }
    }

    private fun RecyclerView.configureList() {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext())
        this.adapter = adapterNews
    }

    private fun updateUIError(message: String?) {
        if (message != null) {
            showSnackbarWithAction(requireView(), message)
        }
    }

    private fun isShowProgressBar(boolean: Boolean) {
        binding.progressBar.isVisible = boolean
    }

    private val viewClick: (News) -> Unit = {
        launchNews(it)
    }

    private val shareClick: (News) -> Unit = {
    }

    private fun launchNews(news: News) {
        val url = SecretConstants.SINGLE_NEWS_URL(news.cmsId)
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
                    SecretConstants.SINGLE_NEWS_URL(news.cmsId), news.title!!
                )
            )
        } else {
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(requireActivity(), Uri.parse(url))
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val news = adapterNews.currentList[item.itemId]
        when (item.order) {
            0 -> launchNews(news)
            1 -> {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    ) {
                        with(requireContext()) {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(getString(R.string.msg_permission_required))
                                .setMessage(getString(R.string.msg_need_permission))
                                .setPositiveButton(getString(R.string.text_ok)) { d, _ ->
                                    requestPermissions(
                                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                        Constants.RC_PERMISSION
                                    )
                                    d.dismiss()
                                }
                                .setNegativeButton(getString(R.string.text_cancel)) { d, _ ->
                                    d.dismiss()
                                }
                                .create()
                                .show()
                        }
                    } else {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ),
                            Constants.RC_PERMISSION
                        )
                    }
                } else {
                    if (!news.attachment.isNullOrBlank()) {
                        val files = news.attachment
                            ?.removeSuffix("||")
                            ?.split("||")
                        Timber.d(files.toString())

                        val attachmentBinding =
                            AttachmentDialogLayoutBinding.inflate(LayoutInflater.from(requireContext()))
                        if (!files.isNullOrEmpty()) {
                            val builder = MaterialAlertDialogBuilder(requireContext())
                                .setView(attachmentBinding.root)
                                .setTitle("Attachment")
                                .create()
                            builder.show()
                            val attachmentAdapter =
                                AttachmentAdapter(files, AttachmentClickListener {
                                    downloadAndOpenFile(it)
                                })
                            attachmentBinding.listFiles.apply {
                                adapter = attachmentAdapter
                                setHasFixedSize(true)
                                layoutManager = LinearLayoutManager(requireContext())
                            }
                        }
                    } else {
                        showSnackbarWithAction(binding.root, getString(R.string.text_no_attachment))
                    }
                }
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_PERMISSION && resultCode == Activity.RESULT_OK) {
            showSnackbarWithAction(
                requireView(),
                requireContext().getString(R.string.msg_permission_granted)
            )
        }
    }

    private fun downloadAndOpenFile(it: String) {
        val downloadManager =
            requireContext().getSystemService<DownloadManager>()
        val fileNameMap = URLConnection.getFileNameMap()
        val request = DownloadManager.Request("${Constants.DAO_TAO_UPLOAD_URL}/$it".toUri())
            .setTitle(it)
            .setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_MOBILE or
                        DownloadManager.Request.NETWORK_WIFI
            )
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                it
            )
            .setMimeType(
                fileNameMap.getContentTypeFor(it)
            )
        requireActivity().registerReceiver(
            AttachmentReceiver(),
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
//                            if (it.substringAfterLast('.', "") in arrayOf(
//                                    "jpg",
//                                    "jpeg",
//                                    "png",
//                                    "webp",
//                                    "gif"
//                                )
//                            ) {
        downloadManager?.enqueue(request)
//                            } else {
//                                downloadManager?.enqueue()
//                            }
    }
}
