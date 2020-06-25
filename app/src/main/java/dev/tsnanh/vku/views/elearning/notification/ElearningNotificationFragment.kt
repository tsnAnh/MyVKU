package dev.tsnanh.vku.views.elearning.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.tsnanh.vku.R
import dev.tsnanh.vku.viewmodels.elearning.ElearningNotificationViewModel

class ElearningNotificationFragment : Fragment() {

    companion object {
        fun newInstance() = ElearningNotificationFragment()
    }

    private lateinit var viewModelElearning: ElearningNotificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_elearning_notification, container, false)
    }

}