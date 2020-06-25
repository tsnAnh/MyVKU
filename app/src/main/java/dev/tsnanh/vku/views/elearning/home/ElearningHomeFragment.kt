package dev.tsnanh.vku.views.elearning.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.tsnanh.vku.R
import dev.tsnanh.vku.viewmodels.elearning.ElearningHomeViewModel

class ElearningHomeFragment : Fragment() {
    private val viewModelElearning: ElearningHomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_elearning_home, container, false)
    }

}