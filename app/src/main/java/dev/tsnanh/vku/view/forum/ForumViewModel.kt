package dev.tsnanh.vku.view.forum

import androidx.lifecycle.*
import dev.tsnanh.vku.database.VKUDatabase
import dev.tsnanh.vku.domain.Forum
import dev.tsnanh.vku.repository.VKURepository
import kotlinx.coroutines.launch

class ForumViewModel(database: VKUDatabase) : ViewModel() {

    private val repository = VKURepository(database)
    val forums = repository.forums

    private val _navigateToListThread = MutableLiveData<Forum>()
    val navigateToListThread: LiveData<Forum>
        get() = _navigateToListThread

    init {
        viewModelScope.launch {
            repository.refreshForums()
        }
    }

    fun onItemClick(forum: Forum) {
        _navigateToListThread.value = forum
    }

    fun onItemClicked() {
        _navigateToListThread.value = null
    }
}

class ForumViewModelFactory(
    private val database: VKUDatabase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForumViewModel::class.java)) {
            return ForumViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
