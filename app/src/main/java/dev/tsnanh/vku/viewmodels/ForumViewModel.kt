/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.domain.Forum
import dev.tsnanh.vku.domain.Resource
import dev.tsnanh.vku.repository.VKURepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class ForumViewModel : ViewModel() {

    private val repository: VKURepository by inject(VKURepository::class.java)

    private val _navigateToListThread = MutableLiveData<Pair<Forum, MaterialCardView>>()
    val navigateToListThread: LiveData<Pair<Forum, MaterialCardView>>
        get() = _navigateToListThread

    private var _forums = repository.getAllForums()
    val forums: LiveData<Resource<List<Forum>>>
        get() = _forums

    suspend fun refreshForums() {
        withContext(Dispatchers.IO) {
            _forums = repository.getAllForums()
        }
    }

    /**
     * Call when item being click
     * @param forum Forum
     */
    fun onItemClick(forum: Pair<Forum, MaterialCardView>) {
        _navigateToListThread.value = forum
    }

    fun onItemClicked() {
        _navigateToListThread.value = null
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}
