package dev.tsnanh.vku.views.elearning.option

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import dev.tsnanh.vku.R
import dev.tsnanh.vku.activities.MainActivity
import dev.tsnanh.vku.databinding.FragmentOptionBinding
import dev.tsnanh.vku.viewmodels.elearning.OptionViewModel

class OptionFragment : Fragment() {

    private val viewModel: OptionViewModel by viewModels()
    private lateinit var binding: FragmentOptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_option, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.backToMyVKU.observe(viewLifecycleOwner, Observer {
            it?.let {
                val sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(requireContext())
                sharedPreferences.edit {
                    putBoolean(requireContext().getString(R.string.elearning_mode), false)
                }
                startActivity(Intent(requireContext(), MainActivity::class.java))
                viewModel.navigatedBackToMyVKU()
                requireActivity().finish()
            }
        })
    }

}