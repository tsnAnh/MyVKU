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
import dev.tsnanh.vku.databinding.FragmentElearningOptionBinding
import dev.tsnanh.vku.viewmodels.elearning.ElearningOptionViewModel

class ElearningOptionFragment : Fragment() {

    private val viewModelElearning: ElearningOptionViewModel by viewModels()
    private lateinit var binding: FragmentElearningOptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_elearning_option, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModelElearning
        binding.lifecycleOwner = viewLifecycleOwner

        viewModelElearning.backToMyVKU.observe(viewLifecycleOwner, Observer {
            it?.let {
                val sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(requireContext())
                sharedPreferences.edit {
                    putBoolean(requireContext().getString(R.string.elearning_mode), false)
                }
                startActivity(Intent(requireContext(), MainActivity::class.java))
                viewModelElearning.navigatedBackToMyVKU()
                requireActivity().finish()
            }
        })
    }

}