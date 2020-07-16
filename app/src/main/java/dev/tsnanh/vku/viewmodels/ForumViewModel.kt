/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.domain.entities.NetworkCustomForum
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.usecases.RetrieveForumsUseCase
import org.koin.java.KoinJavaComponent.inject

class ForumViewModel : ViewModel() {
    private val _navigateToListThread =
        MutableLiveData<Pair<NetworkCustomForum, MaterialCardView>>()
    val navigateToListThread: LiveData<Pair<NetworkCustomForum, MaterialCardView>>
        get() = _navigateToListThread

    private val retrieveForumsUseCase by inject(RetrieveForumsUseCase::class.java)

    private var _forums = retrieveForumsUseCase.execute()
    val forums: LiveData<Resource<List<NetworkCustomForum>>>
        get() = _forums

    fun onItemClick(forum: Pair<NetworkCustomForum, MaterialCardView>) {
        _navigateToListThread.value = forum
    }

    fun onItemClicked() {
        _navigateToListThread.value = null
    }

    fun refreshForums() {
        _forums = retrieveForumsUseCase.execute()
    }
}
