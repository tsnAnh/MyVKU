/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.view.newthread

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
import android.widget.ArrayAdapter
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dev.tsnanh.vku.R
import dev.tsnanh.vku.activities.MainViewModel
import dev.tsnanh.vku.adapters.ImageChooserAdapter
import dev.tsnanh.vku.adapters.ImageChooserClickListener
import dev.tsnanh.vku.databinding.FragmentNewThreadBinding
import dev.tsnanh.vku.domain.ForumThread
import dev.tsnanh.vku.domain.Post
import dev.tsnanh.vku.domain.Resource
import timber.log.Timber

const val RC_IMAGE_PICKER = 0
const val RC_ADD_PHOTO = 1
const val RC_PERMISSION = 100

class NewThreadFragment : Fragment() {

    private val viewModel: NewThreadViewModel by viewModels()
    private lateinit var binding: FragmentNewThreadBinding
    private lateinit var pickerAdapter: ImageChooserAdapter
    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enterTransition = MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.X, false)
//        enterTransition = MaterialContainerTransform(requireContext()).apply {
//            duration = 3000
//        }
//        enterTransition = MaterialFadeThrough.create(requireContext())
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_new_thread, container, false)

        binding.toolbar.apply {
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            title = "Create New Thread"
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val navArgs: NewThreadFragmentArgs by navArgs()

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        navArgs.forumId?.let {
            binding.forum.apply {
                tag = it
                setText(navArgs.forumTitle)
                binding.layoutForum.isEnabled = false
            }
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
        binding.listImageUpload.setHasFixedSize(true)
        binding.listImageUpload.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.listImageUpload.adapter = pickerAdapter

        viewModel.pickerHasImage.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.pickerHasImage = it
            }
        })

        viewModel.forums.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Resource.Loading -> {
                        Timber.d("Loading Forum List...")
                    }
                    is Resource.Success -> {
                        if (it.data != null && it.data.isNotEmpty()) {
                            val forumsTitle = it.data.map { forum ->
                                forum.title
                            }
                            val arrAdapter =
                                ArrayAdapter(
                                    requireContext(),
                                    R.layout.dropdown_menu_popup_item,
                                    forumsTitle
                                )

                            binding.forum.setOnItemClickListener { _, _, i, _ ->
                                binding.forum.tag = it.data[i].id
                                Timber.d(it.data[i].id)
                            }

                            binding.forum.setAdapter(arrAdapter)
                        }
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })

        binding.fabSubmit.setOnClickListener {
            createThread()
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
                        .setTitle("Permission required")
                        .setMessage("We need permission to upload your image!")
                        .setPositiveButton("OK") { d, _ ->
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

    private fun createThread() {
        clearError()
        if (
            binding.title.text.isNullOrBlank() ||
            binding.content.text.isNullOrBlank() ||
            binding.forum.text.isNullOrBlank()
        ) {
            if (binding.title.text.isNullOrBlank()) binding.title.error = "Empty Title"
            if (binding.forum.text.isNullOrBlank()) binding.forum.error = "Choose forum please"
            if (binding.content.text.isNullOrBlank()) binding.content.error = "Empty content"
        } else {
            val thread = prepareThread()
            val post = preparePost()

            activityViewModel.createNewThread(pickerAdapter.currentList, thread, post)
            findNavController().navigateUp()
        }
    }

    private fun prepareThread(): ForumThread {
        return ForumThread(
            title = binding.title.text.toString().trim(),
            forumId = (binding.forum.tag as String?).toString()
        )
    }

    private fun preparePost(): Post {
        return Post(
            content = binding.content.text.toString().trim()
        )
    }

    private fun clearError() {
        binding.title.error = null
        binding.content.error = null
        binding.forum.error = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("uris", ArrayList(pickerAdapter.currentList))
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        pickerAdapter.submitList(savedInstanceState?.getParcelableArrayList("uris"))
        super.onViewStateRestored(savedInstanceState)
    }

}
