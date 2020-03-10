package dev.tsnanh.vku.view.forum

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import dev.tsnanh.vku.R

class ForumFragment : Fragment() {

    companion object {
        fun newInstance() = ForumFragment()
    }

    private lateinit var viewModel: ForumViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forum, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ForumViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
