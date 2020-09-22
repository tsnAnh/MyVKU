package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.*
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import javax.inject.Inject

interface ReplyRepo {
    @Throws(Exception::class)
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

    fun getReplyById(replyId: String): Flow<Resource<NetworkReply>>

    fun getPageCount(threadId: String, limit: Int): Flow<Resource<ReplyContainer>>

    @Throws(Exception::class)
    fun likeOrUnlikeReply(idToken: String, replyId: String): Flow<List<String>>

    suspend fun updateReply(
        idToken: String,
        replyId: String,
        newImage: Array<MultipartBody.Part>? = null,
        content: RequestBody,
        images: Array<RequestBody>? = null,
    ): WorkResult<Reply>
}

class ReplyRepoImpl @Inject constructor() : ReplyRepo {
    @Throws(Exception::class)
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
            Resource.Error(e)
        }
    }

    override fun getAllRepliesInThread(
        threadId: String,
        page: Int,
        limit: Int
    ): Flow<Resource<ReplyContainer>> = flow {
        emit(Resource.Loading())
        try {
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
            emit(Resource.Error<ReplyContainer>(e))
        }
    }

    override fun getReplyById(replyId: String): Flow<Resource<NetworkReply>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(VKUServiceApi.network.getReplyById(replyId)))
        } catch (e: Exception) {
            emit(Resource.Error<NetworkReply>(e))
        }
    }

    override fun getPageCount(threadId: String, limit: Int): Flow<Resource<ReplyContainer>> = flow {
        emit(Resource.Loading())
        try {
            emit(
                Resource.Success(VKUServiceApi.network.getRepliesInThread(threadId, 1, limit))
            )
        } catch (e: Exception) {
            emit(Resource.Error<ReplyContainer>(e))
        }
    }

    @Throws(Exception::class)
    override fun likeOrUnlikeReply(idToken: String, replyId: String): Flow<List<String>> = flow {
        emit(emptyList())
        try {
            emit(VKUServiceApi.network.likeOrUnlikeReply(idToken, replyId))
        } catch (e: Exception) {
            // emit(e)
            throw e
        }
    }

    override suspend fun updateReply(
        idToken: String,
        replyId: String,
        newImage: Array<MultipartBody.Part>?,
        content: RequestBody,
        images: Array<RequestBody>?,
    ): WorkResult<Reply> {
        return try {
            WorkResult.Success(
                VKUServiceApi.network.updateReply(
                    idToken,
                    replyId,
                    newImage,
                    content,
                    images
                )
            )
        } catch (e: Exception) {
            WorkResult.Error(e.message.toString())
        }
    }
}