package dev.tsnanh.vku.views.elearning.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.tsnanh.vku.R
import dev.tsnanh.vku.viewmodels.elearning.ElearningCalendarViewModel

class ElearningCalendarFragment : Fragment() {

    companion object {
        fun newInstance() =
            ElearningCalendarFragment()
    }

    private lateinit var viewModelElearning: ElearningCalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_elearning_calendar, container, false)
    }

}