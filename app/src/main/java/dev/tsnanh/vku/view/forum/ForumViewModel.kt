package dev.tsnanh.vku.view.forum

import androidx.lifecycle.*
import dev.tsnanh.vku.database.VKUDatabase
import dev.tsnanh.vku.domain.Forum
import dev.tsnanh.vku.network.VKUServiceApi
import dev.tsnanh.vku.network.asDomainModel
import dev.tsnanh.vku.repository.VKURepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class ForumViewModel : ViewModel() {

    private val repository: VKURepository by inject(VKURepository::class.java)

    private val _navigateToListThread = MutableLiveData<Forum>()
    val navigateToListThread: LiveData<Forum>
        get() = _navigateToListThread

    val forums = repository.forums

    /**
     * Call when item being click
     * @param forum Forum
     */
    fun onItemClick(forum: Forum) {
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
