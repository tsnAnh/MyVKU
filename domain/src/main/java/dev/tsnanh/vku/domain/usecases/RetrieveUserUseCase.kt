package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.User
import dev.tsnanh.vku.domain.repositories.UserRepo
import org.koin.java.KoinJavaComponent.inject

interface RetrieveUserUseCase {
    fun execute(userId: String): LiveData<Resource<User>>
}

class RetrieveUserUseCaseImpl : RetrieveUserUseCase {
    private val userRepo by inject(UserRepo::class.java)
    override fun execute(userId: String) = liveData {
        emit(Resource.Loading<User>())
        try {
            emit(Resource.Success(userRepo.getUser(userId)))
        } catch (e: Exception) {
            emit(Resource.Error<User>("Something went wrong!"))
        }
    }
}