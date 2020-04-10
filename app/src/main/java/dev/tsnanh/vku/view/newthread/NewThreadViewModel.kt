package dev.tsnanh.vku.view.newthread

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import dev.tsnanh.vku.domain.ForumThread
import dev.tsnanh.vku.domain.Post
import dev.tsnanh.vku.domain.asNetworkModel
import dev.tsnanh.vku.network.NetworkCreateThreadContainer
import dev.tsnanh.vku.network.VKUServiceApi
import dev.tsnanh.vku.network.asDomainModel
import dev.tsnanh.vku.repository.VKURepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class NewThreadViewModel(private val state: SavedStateHandle) : ViewModel() {

    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val repository by inject(VKURepository::class.java)

    val forums = repository.forums

    private val _pickerHasImage = MutableLiveData(false)
    val pickerHasImage: LiveData<Boolean>
        get() = _pickerHasImage

    private val _onThreadCreated = MutableLiveData<ForumThread>()
    val onThreadCreated: LiveData<ForumThread>
        get() = _onThreadCreated

    fun onThreadCreated() {
        _onThreadCreated.value = null
    }

    fun onPickerHasImage() {
        _pickerHasImage.value = true
    }

    fun onPickerHasNoImage() {
        _pickerHasImage.value = false
    }

    fun create(thread: ForumThread, post: Post) {
        val container = NetworkCreateThreadContainer(thread.asNetworkModel(), post.asNetworkModel())
        val task = firebaseUser?.getIdToken(true)
        task?.addOnSuccessListener {
            viewModelScope.launch {
                val responseThread = withContext(Dispatchers.IO) {
                    it.token?.let { it1 ->
                        VKUServiceApi.network.createThread("Bearer $it1", container)
                    }
                }
                _onThreadCreated.value = responseThread?.asDomainModel()
            }
        }
    }
}
