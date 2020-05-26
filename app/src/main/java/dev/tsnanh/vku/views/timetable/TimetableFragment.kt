/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialFadeThrough
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.FragmentTimetableBinding
import dev.tsnanh.vku.viewmodels.TimetableViewModel
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.engine.FlutterEngineCache

class TimetableFragment : Fragment() {

    private val viewModel: TimetableViewModel by viewModels()
    private lateinit var binding: FragmentTimetableBinding
    private var flutterFragment: FlutterFragment? = null

    companion object {
        // Define a tag String to represent the FlutterFragment within this
        // Activity's FragmentManager. This value can be whatever you'd like.
        private const val TAG_FLUTTER_FRAGMENT = "flutter_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough.create()
        exitTransition = MaterialFadeThrough.create()

        flutterFragment =
            childFragmentManager.findFragmentByTag(TAG_FLUTTER_FRAGMENT) as FlutterFragment?


        if (flutterFragment == null) {
            val newFlutterFragment = FlutterFragment.createDefault()
            flutterFragment = newFlutterFragment
            flutterFragment!!.configureFlutterEngine(
                FlutterEngineCache
                    .getInstance()
                    .get("my_engine_id")!!
            )
            childFragmentManager
                .beginTransaction()
                .add(
                    R.id.fragment_container,
                    newFlutterFragment,
                    TAG_FLUTTER_FRAGMENT
                )
                .commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_timetable, container, false)

        return binding.root
    }

}
