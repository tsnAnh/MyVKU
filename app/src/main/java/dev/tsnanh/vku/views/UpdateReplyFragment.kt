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
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ImageChooserAdapter
import dev.tsnanh.vku.adapters.ImageChooserClickListener
import dev.tsnanh.vku.adapters.UpdateReplyImageAdapter
import dev.tsnanh.vku.databinding.FragmentUpdateReplyBinding
import dev.tsnanh.vku.databinding.ProgressDialogLayoutBinding
import dev.tsnanh.vku.domain.entities.NetworkReply
import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.showSnackbar
import dev.tsnanh.vku.viewmodels.UpdateReplyViewModel
import timber.log.Timber

@AndroidEntryPoint
class UpdateReplyFragment : Fragment() {
    private val viewModel: UpdateReplyViewModel by viewModels()
    private lateinit var binding: FragmentUpdateReplyBinding
    private lateinit var pickerAdapter: ImageChooserAdapter
    private lateinit var imagesAdapter: UpdateReplyImageAdapter
    private val navArgs: UpdateReplyFragmentArgs by navArgs()
    private lateinit var progressBarLayoutBinding: ProgressDialogLayoutBinding
    private val progressDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setView(
                progressBarLayoutBinding.root
            )
            .setCancelable(false)
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_update_reply, container, false)

        binding.toolbar.apply {
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
        progressBarLayoutBinding =
            ProgressDialogLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pickerAdapter = ImageChooserAdapter(ImageChooserClickListener(
            listener = { pos ->
                val newList = pickerAdapter.currentList.toMutableList()
                newList.removeAt(pos.minus(imagesAdapter.itemCount))
                if (newList.isEmpty()) {
                    if (imagesAdapter.itemCount <= 0) {
                        viewModel.onPickerHasNoImage()
                    }
                    pickerAdapter.submitList(emptyList())
                } else {
                    viewModel.onPickerHasImage()
                    pickerAdapter.submitList(newList)
                }
            },
            footerClick = {
                pickImage(Constants.RC_ADD_PHOTO)
            }
        ))

        imagesAdapter = UpdateReplyImageAdapter(emptyList(), ImageChooserClickListener(
            listener = listener,
            footerClick = { pickImage(Constants.RC_ADD_PHOTO) }
        ))

        val concatAdapter = ConcatAdapter(imagesAdapter, pickerAdapter)

        binding.listImageUpload.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = concatAdapter
            setHasFixedSize(false)
            isNestedScrollingEnabled = false
        }

        viewModel.updateReplyWorkLiveData.observe<MutableList<WorkInfo>?>(viewLifecycleOwner) { workInfos ->
            if (workInfos.isNullOrEmpty()) {
                return@observe
            }

            val workInfo = workInfos[0]
            if (!progressDialog.isShowing) {
                progressDialog.show()
            }

            if (workInfo.state.isFinished) {
                progressDialog.hide()
                val threadId = workInfo.outputData.getString("threadId")
                Timber.i(threadId)
                WorkManager.getInstance(requireContext()).pruneWork()
                findNavController().navigate(
                    UpdateReplyFragmentDirections.actionNavigationUpdateReplyToNavigationReplies(
                        threadId!!
                    )
                )
            }
        }

        viewModel.reply(navArgs.replyId).observe<Resource<NetworkReply>>(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    binding.apply {
                        userPopulatedReply = resource.data

                        Timber.d(resource.data.toString())
                        fabUpdateReply.setOnClickListener {
                            viewModel.editReply(
                                navArgs.replyId,
                                pickerAdapter.currentList,
                                content.text.toString(),
                                imagesAdapter.listImages
                            )
                        }

                        val list = resource.data?.images
                        if (list != null) {
                            Timber.d(list.toString())
                            imagesAdapter.updateList(list)
                            viewModel.onPickerHasImage()
                        } else {
                            viewModel.onPickerHasNoImage()
                        }
                    }
                }
            }
        }

        viewModel.pickerHasImage.observe<Boolean>(viewLifecycleOwner) {
            it.let {
                binding.pickerHasImage = it
            }
        }

        binding.chooseImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                pickImage(Constants.RC_IMAGE_PICKER)
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(requireContext().getString(R.string.msg_permission_required))
                        .setMessage(requireContext().getString(R.string.msg_need_permission))
                        .setPositiveButton("OK") { d, _ ->
                            pickImage(Constants.RC_IMAGE_PICKER)
                            d.dismiss()
                        }
                        .create().show()
                } else {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constants.RC_PERMISSION
                    )
                }
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
        if (requestCode == Constants.RC_IMAGE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    if (data.clipData != null) {
                        // check if user has selected more than 5 images
                        if (data.clipData!!.itemCount > 5) {
                            showSnackbar(
                                requireView(),
                                requireContext().getString(R.string.msg_pick_image_canceled),
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
                                requireContext().getString(R.string.msg_pick_image_canceled),
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
        } else if (requestCode == Constants.RC_ADD_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    if (data.clipData != null) {
                        if (data.clipData!!.itemCount + pickerAdapter.currentList.size > 5) {
                            showSnackbar(
                                requireView(),
                                requireContext().getString(R.string.msg_pick_image_canceled),
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
                                requireContext().getString(R.string.msg_pick_image_canceled),
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
        if (requestCode == Constants.RC_PERMISSION &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            pickImage(Constants.RC_IMAGE_PICKER)
        }
    }
    // endregion

    private val listener: (Int) -> Unit = { pos ->
        val newList = imagesAdapter.listImages.toMutableList()
        newList.removeAt(pos)
        if (newList.isEmpty()) {
            if (pickerAdapter.itemCount <= 0) {
                viewModel.onPickerHasNoImage()
            }
            imagesAdapter.updateList(emptyList())
            imagesAdapter.notifyDataSetChanged()
        } else {
            viewModel.onPickerHasImage()
            imagesAdapter.updateList(newList)
            imagesAdapter.notifyDataSetChanged()
        }
    }
}