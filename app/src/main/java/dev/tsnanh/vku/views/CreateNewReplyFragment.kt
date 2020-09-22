/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.WorkManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ImageChooserAdapter
import dev.tsnanh.vku.adapters.ImageChooserClickListener
import dev.tsnanh.vku.databinding.FragmentNewReplyBinding
import dev.tsnanh.vku.domain.entities.NetworkReply
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.*
import dev.tsnanh.vku.utils.Constants.Companion.RC_ADD_PHOTO
import dev.tsnanh.vku.utils.Constants.Companion.RC_IMAGE_PICKER
import dev.tsnanh.vku.utils.Constants.Companion.RC_PERMISSION
import dev.tsnanh.vku.viewmodels.CreateNewReplyViewModel
import dev.tsnanh.vku.viewmodels.MainViewModel
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CreateNewReplyFragment : Fragment() {
    private val viewModel: CreateNewReplyViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    private val navArgs: CreateNewReplyFragmentArgs by navArgs()

    private lateinit var binding: FragmentNewReplyBinding
    private lateinit var pickerAdapter: ImageChooserAdapter
    @Inject lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            setPathMotion(MaterialArcMotion())
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_new_reply, container, false)

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.layout.transitionName = Constants.FAB_TRANSFORM_TO_NEW_REPLY

        if (navArgs.quotedReplyId == null) {
            binding.materialCardView.visibility = View.GONE
        }

        pickerAdapter = ImageChooserAdapter(ImageChooserClickListener(
            listener = { pos ->
                val newList = pickerAdapter.currentList.toMutableList()
                newList.removeAt(pos)
                if (newList.isEmpty()) {
                    viewModel.onPickerHasNoImage()
                    pickerAdapter.submitList(emptyList())
                } else {
                    viewModel.onPickerHasImage()
                    pickerAdapter.submitList(newList)
                }
            },
            footerClick = {
                pickImage(RC_ADD_PHOTO)
            }
        ))
        binding.listImageUpload.apply {
            setHasFixedSize(false)
            isNestedScrollingEnabled = false
            layoutManager =
                GridLayoutManager(requireContext(), 2)
            adapter = pickerAdapter
        }

        viewModel.pickerHasImage.observe(viewLifecycleOwner) {
            it?.let {
                binding.pickerHasImage = it
            }
        }

        viewModel.quotedReply(navArgs.quotedReplyId)?.observe(viewLifecycleOwner) {
            it?.let { replyResource ->
                when (replyResource) {
                    is Resource.Error -> Timber.e(replyResource.throwable)
                    is Resource.Success -> {
                        Timber.i("${replyResource.data}")
                        with(binding) {
                            layoutPost.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            materialCardView.visibility = View.VISIBLE
                            reply = replyResource.data
                            datetime.text =
                                replyResource.data?.createdAt?.convertToDateString()
                        }
                    }
                }
            }
        }

        with(binding) {
            content.validate(requireContext().getString(R.string.text_minimum_content_length)) {
                it.isReplyContentIsValidLength()
            }
        }

        binding.chooseImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                pickImage(RC_IMAGE_PICKER)
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(requireContext().getString(R.string.msg_permission_required))
                        .setMessage(requireContext().getString(R.string.msg_need_permission))
                        .setPositiveButton(requireContext().getString(R.string.text_ok)) { d, _ ->
                            pickImage(RC_IMAGE_PICKER)
                            d.dismiss()
                        }
                        .create().show()
                } else {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        RC_PERMISSION
                    )
                }
            }
        }

        viewModel.createNewReplyWorkerData.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isEmpty()) {
                    return@Observer
                }

                val last = it.last()
                if (last.state.isFinished) {
                    val threadId = last.outputData.getString("threadId")
                    workManager.pruneWork()
                    findNavController().navigate(
                        CreateNewReplyFragmentDirections.actionNewReplyFragmentToNavigationReplies(
                            threadId!!, true
                        )
                    )
                }
            }
        })

        binding.fabCreateReply.setOnClickListener {
            if (binding.content.text.isNullOrBlank()) {
                binding.content.error =
                    requireContext().getString(R.string.text_empty_reply_content)
            } else {
                val post = NetworkReply(
                    content = binding.content.text.toString().trim(),
                    images = emptyList(),
                )

                activityViewModel.createNewReply(
                    navArgs.threadId,
                    post,
                    pickerAdapter.currentList,
                    navArgs.quotedReplyId
                )
            }
        }
    }

    // region Pick Image
    private fun pickImage(requestCode: Int) {
        startActivityForResult(
            Intent(
                Intent.ACTION_GET_CONTENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ).apply {
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }, requestCode
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val listImage = ArrayList<Uri>()
        if (requestCode == RC_IMAGE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                pickerAdapter.submitList(emptyList())

                data?.let {
                    if (data.clipData != null) {
                        // check if user has selected more than 5 images
                        if (data.clipData!!.itemCount > 5) {
                            showSnackbar(
                                requireView(),
                                requireContext().getString(R.string.text_maximum_five_images),
                                requireContext().getString(R.string.text_hide)
                            )
                            return@let
                        }

                        val clipData = data.clipData!!
                        for (index in 0 until clipData.itemCount) {
                            listImage.add(clipData.getItemAt(index).uri)
                        }
                    } else {
                        if (pickerAdapter.currentList.size < 5) {
                            data.data?.let { it1 -> listImage.add(it1) }
                        } else {
                            showSnackbar(
                                requireView(),
                                requireContext().getString(R.string.text_maximum_five_images),
                                requireContext().getString(R.string.text_hide)
                            )
                            return@let
                        }
                    }
                    pickerAdapter.submitList(listImage)

                    viewModel.onPickerHasImage()
                }
            } else {
                viewModel.onPickerHasNoImage()
            }
        } else if (requestCode == RC_ADD_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    if (data.clipData != null) {
                        if (data.clipData!!.itemCount + pickerAdapter.currentList.size > 5) {
                            showSnackbar(
                                requireView(),
                                requireContext().getString(R.string.text_maximum_five_images),
                                requireContext().getString(R.string.text_hide)
                            )
                            return@let
                        }
                        val clipData = data.clipData!!
                        for (index in 0 until clipData.itemCount) {
                            listImage.add(clipData.getItemAt(index).uri)
                        }
                    } else {
                        if (pickerAdapter.currentList.size < 5) {
                            data.data?.let { it1 -> listImage.add(it1) }
                        } else {
                            showSnackbar(
                                requireView(),
                                requireContext().getString(R.string.text_maximum_five_images),
                                requireContext().getString(R.string.text_hide)
                            )
                            return@let
                        }
                    }
                    val list = pickerAdapter.currentList.toMutableList()
                    list.addAll(listImage)
                    pickerAdapter.submitList(list)
                }
            } else {
                showSnackbar(
                    requireView(),
                    requireContext().getString(R.string.msg_pick_image_canceled),
                    requireContext().getString(R.string.text_hide)
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_PERMISSION &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            pickImage(RC_IMAGE_PICKER)
        }
    }
    // endregion

}
