package dev.tsnanh.vku.domain.usecases

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dev.tsnanh.vku.domain.entities.ForumContainer
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.ForumRepo
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent.inject
import retrofit2.HttpException
import java.net.SocketTimeoutException

interface RetrieveForumsUseCase {
    fun execute(): LiveData<Resource<ForumContainer>>
}

class RetrieveForumsUseCaseImpl : RetrieveForumsUseCase {
    private val forumRepo by inject(ForumRepo::class.java)
    override fun execute() = liveData(Dispatchers.IO) {
        emit(Resource.Loading<ForumContainer>())
        try {
            emit(Resource.Success(forumRepo.getForums()))
        } catch (e: SocketTimeoutException) {
            emit(Resource.Error<ForumContainer>("Connection Timed Out"))
        } catch (e2: HttpException) {
            emit(Resource.Error<ForumContainer>("Cannot connect to server!"))
        } catch (t: Throwable) {
            emit(Resource.Error<ForumContainer>("Something went wrong!"))
            Log.d("ERROR", "execute: ${t.message}")
        }
    }
}