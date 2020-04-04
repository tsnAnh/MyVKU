package dev.tsnanh.vku.view.newthread

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import dev.tsnanh.vku.domain.Forum
import dev.tsnanh.vku.domain.ForumThread
import dev.tsnanh.vku.domain.Post
import dev.tsnanh.vku.domain.asNetworkModel
import dev.tsnanh.vku.network.NetworkCreateThreadContainer
import dev.tsnanh.vku.network.VKUServiceApi
import dev.tsnanh.vku.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewThreadViewModel(private val state: SavedStateHandle) : ViewModel() {

    private val firebaseUser = FirebaseAuth.getInstance().currentUser

    private val _forums = MutableLiveData<List<Forum>>()
    val forums: LiveData<List<Forum>>
        get() = _forums

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _forums.postValue(VKUServiceApi.network.getAllSubForums().asDomainModel())
            }
        }
    }

    private val _pickerHasImage = MutableLiveData(false)
    val pickerHasImage: LiveData<Boolean>
        get() = _pickerHasImage

    fun onPickerHasImage() {
        _pickerHasImage.value = true
    }

    fun onPickerHasNoImage() {
        _pickerHasImage.value = false
    }

    fun uploadThread(forumThread: ForumThread, post: Post) {
        val networkThread = forumThread.asNetworkModel()
        val networkPost = post.asNetworkModel()
        val container = NetworkCreateThreadContainer(networkThread, networkPost)
        val task = firebaseUser?.getIdToken(true)
        task?.addOnSuccessListener {
            Log.d(this@NewThreadViewModel.javaClass.simpleName, it.token.toString())
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val token = it.token as String
                    VKUServiceApi.network.createThread(
                        token,
                        container,
                        emptyList()
                    )
                }
            }
        }
    }
}
