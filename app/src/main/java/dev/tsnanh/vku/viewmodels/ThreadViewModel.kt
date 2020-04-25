/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.lifecycle.*
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.domain.ForumThread
import dev.tsnanh.vku.domain.Resource
import dev.tsnanh.vku.network.VKUServiceApi
import dev.tsnanh.vku.network.asDomainModel
import dev.tsnanh.vku.repository.VKURepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import retrofit2.HttpException
import java.net.SocketTimeoutException

class ThreadViewModel(private val forumId: String) : ViewModel() {
    private val repository: VKURepository by inject(VKURepository::class.java)

    private var _threads = MutableLiveData<Resource<List<ForumThread>>>()
    val threads: LiveData<Resource<List<ForumThread>>>
        get() = _threads

    init {
        refreshThreads()
    }

    fun refreshThreads() = viewModelScope.launch(Dispatchers.IO) {
        _threads.postValue(Resource.Loading())
        _threads.postValue(
            try {
                Resource.Success(VKUServiceApi.network.getThreadsInForum(forumId).asDomainModel())
            } catch (e: SocketTimeoutException) {
                Resource.Error<List<ForumThread>>("Connection Timed Out")
            } catch (e2: HttpException) {
                Resource.Error<List<ForumThread>>("Cannot connect to server!")
            } catch (t: Throwable) {
                Resource.Error<List<ForumThread>>("Something went wrong!")
            }
        )
    }

    val forum = repository.getForumById(forumId)

    private val _navigateToReplies = MutableLiveData<Pair<ForumThread, MaterialCardView>>()
    val navigateToReplies: LiveData<Pair<ForumThread, MaterialCardView>>
        get() = _navigateToReplies

    fun onNavigateToReplies(thread: ForumThread, cardView: MaterialCardView) {
        _navigateToReplies.value = thread to cardView
    }

    fun onNavigatedToReplies() {
        _navigateToReplies.value = null
    }
}

class ThreadViewModelFactory(
    private val forumId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThreadViewModel::class.java)) {
            return ThreadViewModel(forumId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
