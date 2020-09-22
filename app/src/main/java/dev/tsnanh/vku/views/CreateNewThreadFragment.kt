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
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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
import dev.tsnanh.vku.databinding.FragmentCreateNewThreadBinding
import dev.tsnanh.vku.databinding.ProgressDialogLayoutBinding
import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.entities.NetworkReply
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.*
import dev.tsnanh.vku.viewmodels.CreateNewThreadViewModel
import dev.tsnanh.vku.viewmodels.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CreateNewThreadFragment : Fragment() {
    private val viewModel: CreateNewThreadViewModel by viewModels()
    private lateinit var binding: FragmentCreateNewThreadBinding
    private lateinit var pickerAdapter: ImageChooserAdapter
    private val activityViewModel: MainViewModel by activityViewModels()
    private lateinit var progressBarLayoutBinding: ProgressDialogLayoutBinding

    private val progressDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setView(progressBarLayoutBinding.root)
            .setCancelable(false)
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            setPathMotion(MaterialArcMotion())
        }
        sharedElementReturnTransition = MaterialContainerTransform().apply {
            setPathMotion(MaterialArcMotion())
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_create_new_thread, container, false)

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

        val navArgs: CreateNewThreadFragmentArgs by navArgs()

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.forum.keyListener = null
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
                pickImage(Constants.RC_ADD_PHOTO)
            }
        ))
        with(binding) {
            title.validate(requireContext().getString(R.string.text_minimum_title_length)) {
                it.isThreadTitleValidLength()
            }
            content.validate(requireContext().getString(R.string.text_minimum_content_length)) {
                it.isReplyContentIsValidLength()
            }
        }
        binding.listImageUpload.apply {
            setHasFixedSize(false)
            isNestedScrollingEnabled = false
            layoutManager =
                GridLayoutManager(
                    requireContext(), 2
                )
            adapter = pickerAdapter
        }

        viewModel.pickerHasImage.observe(viewLifecycleOwner) { it ->
            it?.let {
                binding.pickerHasImage = it
            }
        }

        viewModel.newReplyWorkLiveData.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                return@observe
            }

            val workInfo = it[0]
            if (!progressDialog.isShowing) {
                progressDialog.show()
            }

            if (workInfo.state.isFinished) {
                progressDialog.dismiss()
                val threadId = workInfo.outputData.getString("threadId")
                Timber.d(threadId)
                WorkManager.getInstance(requireContext()).pruneWork()
                findNavController().navigateUp()
            }
        }

        viewModel.navigateToReplyFragment.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(
                    CreateNewThreadFragmentDirections.actionNavigationNewThreadToNavigationReplies(
                        it
                    )
                )
                viewModel.onNavigatedToReplyFragment()
            }
        }

        viewModel.forums.observe(viewLifecycleOwner) { resource ->
            resource?.let {
                when (resource) {
                    is Resource.Loading -> {
                        binding.layoutForum.hint =
                            requireContext().getString(R.string.text_loading_forums)
                    }
                    is Resource.Error -> {
                        binding.layoutForum.error = "Load failed!"
                    }
                    is Resource.Success -> {
                        val forums = resource.data ?: emptyList()
                        if (forums.isNotEmpty()) {
                            binding.layoutForum.hint = requireContext().getString(R.string.text_forum)
                            val forumsTitle = forums.map { forum ->
                                forum.title
                            }
                            val arrAdapter =
                                ArrayAdapter(
                                    requireContext(),
                                    R.layout.dropdown_menu_popup_item,
                                    forumsTitle
                                )

                            binding.forum.setOnItemClickListener { _, _, i, _ ->
                                binding.forum.tag = forums[i].id
                            }

                            binding.forum.setAdapter(arrAdapter)
                        } else {
                            binding.layoutForum.isEnabled = false
                            binding.layoutForum.hint = requireContext()
                                .getString(R.string.text_application_has_no_forums)
                        }
                    }
                }
            }
            /*forums?.let {
                binding.layoutForum.hint = "Forums"
                if (forums.isNotEmpty()) {
                    val forumsTitle = forums.map { forum ->
                        forum.title
                    }
                    val arrAdapter =
                        ArrayAdapter(
                            requireContext(),
                            R.layout.dropdown_menu_popup_item,
                            forumsTitle
                        )

                    binding.forum.setOnItemClickListener { _, _, i, _ ->
                        binding.forum.tag = forums[i].id
                    }

                    binding.forum.setAdapter(arrAdapter)
                } else {
                    binding.layoutForum.isEnabled = false
                    binding.layoutForum.hint = requireContext()
                        .getString(R.string.text_application_has_no_forums)
                }
            }*/
        }

        viewModel.forumSaveState?.observe(viewLifecycleOwner) { forumBundle ->
            with(binding) {
                forum.tag = forumBundle["forumId"] as? String
                forum.setText(forumBundle["forumTitle"] as? String)
            }
        }

        binding.fabSubmit.setOnClickListener {
            createThread()
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
                        .setPositiveButton(requireContext().getString(R.string.text_ok)) { d, _ ->
                            requestPermissions(
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                Constants.RC_PERMISSION
                            )
                            d.dismiss()
                        }
                        .setNegativeButton(requireContext().getString(R.string.text_cancel)) { d, _ ->
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
                pickerAdapter.submitList(emptyList())

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
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.RC_PERMISSION &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            pickImage(Constants.RC_IMAGE_PICKER)
        }
    }
    // endregion

    private fun createThread() {
        when {
            binding.title.text.isNullOrBlank() || binding.content.text.isNullOrBlank() || binding.forum.text.isNullOrBlank() -> {
                if (binding.title.text.isNullOrBlank()) binding.title.error =
                    requireContext().getString(R.string.msg_empty_title)
                if (binding.forum.text.isNullOrBlank()) binding.forum.error =
                    requireContext().getString(R.string.msg_choose_a_forum)
                if (binding.content.text.isNullOrBlank()) binding.content.error =
                    requireContext().getString(R.string.msg_empty_content)
            }
            else -> {
                val thread = prepareThread()
                val reply = prepareReply()

                activityViewModel.createNewThread(pickerAdapter.currentList, thread, reply)
            }
        }
    }

    private fun prepareThread() = ForumThread(title = binding.title.text.toString().trim(),
        forumId = (binding.forum.tag as String?).toString(), likes = emptyList())

    private fun prepareReply() =
        NetworkReply(content = binding.content.text.toString().trim())

    // Disabled for two months
    // TODO: Make it better
/*    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(Constants.URIS_KEY, ArrayList(pickerAdapter.currentList))
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        pickerAdapter.submitList(savedInstanceState?.getParcelableArrayList(Constants.URIS_KEY))
        super.onViewStateRestored(savedInstanceState)
    }*/

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val bundle = Bundle().apply {
            putString("forumId", binding.forum.tag?.toString())
            putString("forumTitle", binding.forum.text.toString())
        }

        outState.putBundle("forum", bundle)
    }

}
