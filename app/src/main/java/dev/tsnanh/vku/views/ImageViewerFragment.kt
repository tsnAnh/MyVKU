/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ImageViewerPagerAdapter
import dev.tsnanh.vku.databinding.FragmentImageViewerBinding
import dev.tsnanh.vku.domain.network.BASE_URL
import dev.tsnanh.vku.utils.Constants.Companion.RC_PERMISSION
import dev.tsnanh.vku.viewmodels.ImageViewerViewModel
import timber.log.Timber

@AndroidEntryPoint
class ImageViewerFragment : Fragment() {
    private lateinit var viewModel: ImageViewerViewModel
    private lateinit var binding: FragmentImageViewerBinding
    private val navArgs by navArgs<ImageViewerFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()
        sharedElementReturnTransition = MaterialContainerTransform()

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_image_viewer, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navArgs.urls.map {
            Timber.d(it)
        }
        val adapter = ImageViewerPagerAdapter(navArgs.urls)

        binding.viewPager2.apply {
            setAdapter(adapter)
            setCurrentItem(navArgs.position, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    when (state) {
                        ViewPager2.SCROLL_STATE_DRAGGING -> binding.download.hide()
                        ViewPager2.SCROLL_STATE_IDLE -> binding.download.show()
                        ViewPager2.SCROLL_STATE_SETTLING -> binding.download.hide()
                    }
                }
            })
        }

        binding.download.setOnClickListener {
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
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(requireContext().getString(R.string.msg_permission_required))
                        .setMessage(requireContext().getString(R.string.msg_need_permission))
                        .setPositiveButton(requireContext().getString(R.string.text_ok)) { d, _ ->
                            ActivityCompat.requestPermissions(
                                requireActivity(),
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                RC_PERMISSION
                            )
                            d.dismiss()
                        }
                        .setNegativeButton(requireContext().getString(R.string.text_cancel)) { d, _ ->
                            d.dismiss()
                        }
                        .create().show()
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        RC_PERMISSION
                    )
                }
            } else {
                downloadImage("$BASE_URL/images/${navArgs.urls[binding.viewPager2.currentItem]}")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RC_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadImage("$BASE_URL/images/${navArgs.urls[binding.viewPager2.currentItem]}")
                }
            }
        }
    }

    private fun downloadImage(url: String) {
        val request = DownloadManager.Request(Uri.parse(url))

        val filename =
            URLUtil.guessFileName(
                url, null, MimeTypeMap.getFileExtensionFromUrl(url)
            )

        request.setTitle(filename)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
        request.setNotificationVisibility(
            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
        )
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI
        )

        val manager =
            requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        manager.enqueue(request)
    }

}
