package dev.tsnanh.vku.view.newthread

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ImageChooserAdapter
import dev.tsnanh.vku.adapters.ImageChooserClickListener
import dev.tsnanh.vku.databinding.FragmentNewThreadBinding
import dev.tsnanh.vku.domain.ForumThread
import dev.tsnanh.vku.domain.Post

const val RC_IMAGE_PICKER = 0
const val RC_ADD_PHOTO = 1

class NewThreadFragment : Fragment() {

    private val viewModel: NewThreadViewModel by viewModels()
    private lateinit var binding: FragmentNewThreadBinding
    private lateinit var pickerAdapter: ImageChooserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        pickerAdapter = ImageChooserAdapter(ImageChooserClickListener(
            listener = {
                val newList = pickerAdapter.currentList.toMutableList()
                newList.removeAt(it)
                if (newList.isEmpty()) {
                    viewModel.onPickerHasNoImage()
                    pickerAdapter.submitList(emptyList())
                } else {
                    viewModel.onPickerHasImage()
                    pickerAdapter.submitList(newList)
                }
                pickerAdapter.notifyDataSetChanged()
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
                val forumsTitle = it.map { forum ->
                    forum.title
                }
                val arrAdapter =
                    ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, forumsTitle)

                binding.forum.setAdapter(arrAdapter)
            }
        })

        binding.fabSubmit.setOnClickListener {
            createThread()
        }

        binding.chooseImage.setOnClickListener {
            pickImage(RC_IMAGE_PICKER)
        }
    }

    // region Pick Image
    private fun pickImage(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, requestCode)
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
        if (binding.title.text.isNullOrBlank()
            || binding.content.text.isNullOrBlank()
            || binding.forum.text.isNullOrBlank()
        ) {
            if (binding.title.text.isNullOrBlank()) binding.title.error = "Empty Title"
            if (binding.forum.text.isNullOrBlank()) binding.forum.error = "Choose forum please"
            if (binding.content.text.isNullOrBlank()) binding.content.error = "Empty content"
        } else {
            val thread = ForumThread(
                title = binding.title.text.toString()
            )
            val post = Post(
                content = binding.content.text.toString()
            )
            viewModel.uploadThread(thread, post)
        }
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
