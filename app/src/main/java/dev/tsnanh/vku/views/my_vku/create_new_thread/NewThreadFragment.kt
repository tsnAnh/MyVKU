/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views.my_vku.create_new_thread

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
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
import androidx.work.WorkManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ImageChooserAdapter
import dev.tsnanh.vku.adapters.ImageChooserClickListener
import dev.tsnanh.vku.databinding.FragmentNewThreadBinding
import dev.tsnanh.vku.databinding.ProgressDialogLayoutBinding
import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.Constants.Companion.THREAD_KEY
import dev.tsnanh.vku.utils.sendNotification
import dev.tsnanh.vku.utils.showSnackbarWithAction
import dev.tsnanh.vku.viewmodels.my_vku.MainViewModel
import dev.tsnanh.vku.viewmodels.my_vku.NewThreadViewModel
import dev.tsnanh.vku.workers.WorkUtil
import timber.log.Timber

class NewThreadFragment : Fragment() {

    private val viewModel: NewThreadViewModel by viewModels()
    private lateinit var binding: FragmentNewThreadBinding
    private lateinit var pickerAdapter: ImageChooserAdapter
    private val activityViewModel: MainViewModel by activityViewModels()

    private lateinit var progressBarLayoutBinding: ProgressDialogLayoutBinding
    private val progressDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setView(
                progressBarLayoutBinding.root
            )
            .setTitle(requireContext().getString(R.string.msg_creating_your_thread))
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
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_new_thread, container, false)

        binding.toolbar.apply {
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
        progressBarLayoutBinding =
            ProgressDialogLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val navArgs: NewThreadFragmentArgs by navArgs()

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
        binding.listImageUpload.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager =
                LinearLayoutManager(
                    requireContext()
                )
            adapter = pickerAdapter
        }

        viewModel.pickerHasImage.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.pickerHasImage = it
            }
        })

        viewModel.createThreadWorkerLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                return@Observer
            }

            val workInfo = it[0]

            if (progressDialog.isShowing) {
                progressBarLayoutBinding.progress.progress =
                    workInfo.progress.getInt(WorkUtil.Progress, 0)
            } else {
                progressDialog.show()
            }

            if (workInfo.state.isFinished) {
                progressDialog.dismiss()
                val jsonAdapter =
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                        .adapter(ForumThread::class.java)
                val json = workInfo.outputData.getString(THREAD_KEY)
                if (json != null && json.isNotEmpty()) {
                    val thread = jsonAdapter.fromJson(json)
                    thread?.let {
                        (requireContext()
                            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                            .sendNotification(
                                thread.title,
                                thread.title + requireContext().getString(R.string.msg_successfully_created),
                                thread.id,
                                thread.title,
                                requireContext()
                            )
                    }
                }
                WorkManager.getInstance(requireContext()).pruneWork()
                findNavController().navigateUp()
            }
        })

        viewModel.forums.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Resource.Loading -> {
                        binding.layoutForum.hint =
                            requireContext().getString(R.string.text_loading_forums)
                    }
                    is Resource.Success -> {
                        binding.layoutForum.hint = "Forums"
                        if (it.data != null && it.data!!.isNotEmpty()) {
                            val forumsTitle = it.data!!.map { forum ->
                                forum.title
                            }
                            val arrAdapter =
                                ArrayAdapter(
                                    requireContext(),
                                    R.layout.dropdown_menu_popup_item,
                                    forumsTitle
                                )

                            binding.forum.setOnItemClickListener { _, _, i, _ ->
                                binding.forum.tag = it.data!![i].id
                            }

                            binding.forum.setAdapter(arrAdapter)
                        }
                    }
                    is Resource.Error -> {
                        binding.apply {
                            this.chooseImage.isEnabled = false
                            this.layoutForum.isEnabled = false
                            this.fabSubmit.isEnabled = false
                            this.layoutTitle.isEnabled = false
                            this.layoutContent.isEnabled = false
                        }
                        showSnackbarWithAction(
                            requireView(),
                            "${it.message.toString()}. Please try again later.",
                            "BACK"
                        ) {
                            findNavController().navigateUp()
                        }
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
        Timber.d("YOoooooooooooooooooooooooooooooooo")
    }

    // region Pick Image
    private fun pickImage(requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val listImage = ArrayList<Uri>()
        if (requestCode == Constants.RC_IMAGE_PICKER) {
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
                viewModel.onPickerHasNoImage()
            }
        } else if (requestCode == Constants.RC_ADD_PHOTO) {
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
                showSnackbarWithAction(
                    requireView(),
                    requireContext().getString(R.string.msg_pick_image_canceled),
                    requireContext().getString(R.string.text_hide)
                )
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
            if (binding.title.text.isNullOrBlank()) binding.title.error =
                requireContext().getString(R.string.msg_empty_title)
            if (binding.forum.text.isNullOrBlank()) binding.forum.error =
                requireContext().getString(R.string.msg_choose_a_forum)
            if (binding.content.text.isNullOrBlank()) binding.content.error =
                requireContext().getString(R.string.msg_empty_content)
        } else {
            val thread = prepareThread()
            val post = preparePost()

            activityViewModel.createNewThread(pickerAdapter.currentList, thread, post)
        }
    }

    private fun prepareThread(): ForumThread {
        return ForumThread(
            title = binding.title.text.toString().trim(),
            forumId = (binding.forum.tag as String?).toString()
        )
    }

    private fun preparePost(): Reply {
        return Reply(
            content = binding.content.text.toString().trim()
        )
    }

    private fun clearError() {
        binding.title.error = null
        binding.content.error = null
        binding.forum.error = null
    }
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

}
