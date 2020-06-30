package dev.tsnanh.vku.domain.repositories

import android.util.Log
import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.domain.entities.ReplyContainer
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.UserPopulatedNetworkReply
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ReplyRepo {
    suspend fun createReply(
        idToken: String,
        threadId: String,
        content: RequestBody,
        images: Array<MultipartBody.Part>? = null,
        quoted: RequestBody? = null
    ): Resource<Reply>

    fun getAllRepliesInThread(
        threadId: String,
        page: Int,
        limit: Int
    ): Flow<Resource<ReplyContainer>>

    fun getReplyById(replyId: String): Flow<Resource<UserPopulatedNetworkReply>>

    fun getPageCount(threadId: String, limit: Int): Flow<Resource<ReplyContainer>>
}

class ReplyRepoImpl : ReplyRepo {
    override suspend fun createReply(
        idToken: String,
        threadId: String,
        content: RequestBody,
        images: Array<MultipartBody.Part>?,
        quoted: RequestBody?
    ): Resource<Reply> {
        return try {
            Resource.Loading<Reply>()
            Resource.Success(
                VKUServiceApi.network.newReply(
                    idToken,
                    threadId,
                    content,
                    images,
                    quoted
                )
            )
        } catch (e: Exception) {
            ErrorHandler.handleError(e)
        }
    }

    override fun getAllRepliesInThread(
        threadId: String,
        page: Int,
        limit: Int
    ): Flow<Resource<ReplyContainer>> = flow {
        emit(Resource.Loading())
        delay(200)
        try {
            Log.i("information: ", "start emitting replies...")
            emit(
                Resource.Success(
                    VKUServiceApi.network.getRepliesInThread(
                        threadId,
                        page,
                        limit
                    )
                )
            )
            delay(10000)
        } catch (e: Exception) {
            emit(ErrorHandler.handleError(e))
        }
    }

    override fun getReplyById(replyId: String): Flow<Resource<UserPopulatedNetworkReply>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(VKUServiceApi.network.getReplyById(replyId)))
        } catch (e: Exception) {
            emit(ErrorHandler.handleError(e))
        }
    }

    override fun getPageCount(threadId: String, limit: Int): Flow<Resource<ReplyContainer>> = flow {
        emit(Resource.Loading())
        try {
            emit(
                Resource.Success(VKUServiceApi.network.getRepliesInThread(threadId, 1, limit))
            )
        } catch (e: Exception) {
            emit(ErrorHandler.handleError(e))
        }
    }
}