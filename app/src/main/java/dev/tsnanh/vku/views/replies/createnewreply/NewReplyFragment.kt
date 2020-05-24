/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views.replies.createnewreply

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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ImageChooserAdapter
import dev.tsnanh.vku.adapters.ImageChooserClickListener
import dev.tsnanh.vku.databinding.FragmentNewReplyBinding
import dev.tsnanh.vku.domain.Post
import dev.tsnanh.vku.domain.Resource
import dev.tsnanh.vku.viewmodels.NewReplyViewModel
import dev.tsnanh.vku.viewmodels.NewThreadViewModelFactory
import dev.tsnanh.vku.views.createnewthread.RC_ADD_PHOTO
import dev.tsnanh.vku.views.createnewthread.RC_IMAGE_PICKER
import dev.tsnanh.vku.views.createnewthread.RC_PERMISSION
import timber.log.Timber

class NewReplyFragment(
    private val threadId: String,
    private val threadTitle: String,
    private val quotedPostId: String = ""
) : BottomSheetDialogFragment() {

    private lateinit var viewModel: NewReplyViewModel
    private lateinit var binding: FragmentNewReplyBinding
    private lateinit var pickerAdapter: ImageChooserAdapter
    private val sharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_new_reply, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = NewThreadViewModelFactory(
            quotedPostId,
            requireActivity().application
        )
        viewModel = ViewModelProvider(this, factory).get(NewReplyViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

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
        binding.listImageUpload.setHasFixedSize(true)
        binding.listImageUpload.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.listImageUpload.adapter = pickerAdapter

        viewModel.quotedPost.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Timber.d("${it.data}")
                when (it) {
                    is Resource.Loading -> {
                        binding.materialCardView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.layoutPost.visibility = View.INVISIBLE
                    }
                    is Resource.Error -> {
                        // handle error
                        if (it.message == "empty")
                            binding.materialCardView.visibility = View.GONE
                        else
                            Snackbar
                                .make(
                                    requireView(),
                                    "${it.message}",
                                    Snackbar.LENGTH_LONG
                                )
                                .show()
                    }
                    is Resource.Success -> {
                        binding.post = it.data
                        binding.progressBar.visibility = View.GONE
                        binding.layoutPost.visibility = View.VISIBLE
                    }
                }
            }
        })

        viewModel.pickerHasImage.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.pickerHasImage = it
            }
        })

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
                        .setTitle("Permission required")
                        .setMessage("We need permission to upload your image!")
                        .setPositiveButton("OK") { d, _ ->
                            pickImage(RC_IMAGE_PICKER)
                            d.dismiss()
                        }
                        .create().show()
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        RC_PERMISSION
                    )
                }
            }
        }

        viewModel.navigateBack.observe(viewLifecycleOwner, Observer {
            it?.let {
                dismiss()
                viewModel.onNavigatedBack()
            }
        })

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.submit.setOnClickListener {
            if (binding.content.text.isNullOrBlank()) {
                binding.content.error = "Empty Reply Content"
            } else {
                val post = Post(
                    content = binding.content.text.toString().trim(),
                    threadTitle = threadTitle,
                    threadId = threadId,
                    quoted = if (quotedPostId.isNotBlank()) quotedPostId else ""
                )

                viewModel.newReply(post, pickerAdapter.currentList)
            }
        }
    }

    // region Pick Image
    private fun pickImage(requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            RC_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage(RC_IMAGE_PICKER)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val listImage = ArrayList<Uri>()
        if (requestCode == RC_IMAGE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                pickerAdapter.submitList(emptyList())
                data?.let {
                    if (data.clipData != null) {
                        val clipData = data.clipData!!
                        for (index in 0 until clipData.itemCount) {
                            listImage.add(clipData.getItemAt(index).uri)
                        }
                    } else {
                        data.data?.let { it1 -> listImage.add(it1) }
                    }
                    pickerAdapter.submitList(listImage)

                    viewModel.onPickerHasImage()
                }
            } else {
                Snackbar.make(
                    requireView(),
                    "Pick image canceled!",
                    Snackbar.LENGTH_SHORT
                ).show()
                viewModel.onPickerHasNoImage()
            }
        } else if (requestCode == RC_ADD_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    if (data.clipData != null) {
                        val clipData = data.clipData!!
                        for (index in 0 until clipData.itemCount) {
                            listImage.add(clipData.getItemAt(index).uri)
                        }
                    } else {
                        data.data?.let { it1 -> listImage.add(it1) }
                    }
                    val list = pickerAdapter.currentList.toMutableList()
                    list.addAll(listImage)
                    pickerAdapter.submitList(list)
                }
            } else {
                Snackbar.make(requireView(), "No image selected!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    // endregion

}
