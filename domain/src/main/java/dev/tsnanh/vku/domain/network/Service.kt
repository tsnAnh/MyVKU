/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */
package dev.tsnanh.vku.domain.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.tsnanh.vku.domain.entities.Forum
import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.entities.LoginBody
import dev.tsnanh.vku.domain.entities.LoginResponse
import dev.tsnanh.vku.domain.entities.NetworkCustomForum
import dev.tsnanh.vku.domain.entities.NetworkForumThreadCustom
import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.domain.entities.Notification
import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.domain.entities.ReplyContainer
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.domain.entities.Teacher
import dev.tsnanh.vku.domain.entities.User
import dev.tsnanh.vku.domain.entities.UserPopulatedNetworkReply
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

/**
 * Base URL
 */
// Google Cloud VM Instance Server
const val BASE_URL = "http://34.87.151.214:5001"

/**
 * OkHttp client
 */
private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

/**
 * Moshi JSON converter
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Retrofit client
 */
private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface VKUService {

    // region Forum routes
    /**
     * Get all forums from REST API
     * @return forums List<NetworkCustomForum>
     */
    @GET("api/forum")
    suspend fun getForums(): List<NetworkCustomForum>

    /**
     * Get a forum by id
     * @param forumId String
     * @method GET
     * @return forum Forum
     */
    @GET("api/forum/{forumId}")
    suspend fun getForumById(@Path("forumId") forumId: String): Forum

    /**
     * Create new thread in specified forum
     * @param idToken String
     * @param thread ForumThread
     * @param forumId String
     * @method POST
     * @return thread ForumThread
     */
    @POST("api/thread/{forumId}")
    suspend fun createThread(
        @Header("gg-auth-token") idToken: String,
        @Body thread: ForumThread,
        @Path("forumId") forumId: String
    ): ForumThread

    // endregion
    /**
     * Get all threads from a specified forum
     * @param forumId String
     * @method GET
     * @return threads List<NetworkForumThreadCustom>
     */
    @GET("api/forum/thread/{forumId}")
    suspend fun getThreads(@Path("forumId") forumId: String): List<NetworkForumThreadCustom>

    /**
     * Get a user by id from REST API
     * @param userId String
     * @method GET
     * @return User
     */
    @GET("u/get/{userId}")
    suspend fun getUserById(@Path("userId") userId: String): User

    /**
     * Get all replies in specified thread
     * @param threadId String
     * @param page Int
     * @param limit Int
     * @method GET
     * @return replyContainer ReplyContainer
     */
    @GET("api/thread/reply/{threadId}")
    suspend fun getRepliesInThread(
        @Path("threadId") threadId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ReplyContainer

    /**
     * Get specific thread by id
     * @author tsnAnh
     * @param threadId String
     * @return Thread
     */
    @GET("api/thread/{threadId}")
    suspend fun getThreadById(@Path("threadId") threadId: String): Thread

    /**
     * Create a new reply
     * @author tsnAnh
     * @param idToken String
     * @param threadId String
     * @param content RequestBody
     * @param images Array<MultipartBody.Part>
     * @param quoted RequestBody
     * @return Reply
     */
    @Multipart
    @POST("api/reply/{threadId}")
    suspend fun newReply(
        @Header("gg-auth-token") idToken: String,
        @Path("threadId") threadId: String,
        @Part("content") content: RequestBody,
        @Part images: Array<MultipartBody.Part>? = null,
        @Part("quoted") quoted: RequestBody? = null
    ): Reply

    /**
     * Get a reply by id
     * @author tsnAnh
     * @param id String
     * @return UserPopulatedNetworkReply
     */
    @GET("/api/reply/{idReply}")
    suspend fun getReplyById(@Path("idReply") id: String): UserPopulatedNetworkReply

    /**
     * Login
     * @author tsnAnh
     * @param idToken String
     * @return response LoginResponse
     */
    @POST("api/user/auth")
    suspend fun login(
        @Header("gg-auth-token") idToken: String,
        @Body body: LoginBody
    ): LoginResponse

    /**
     * Retrieve user's timetable
     * @author tsnAnh
     * @param email String
     * @return subjects List<Subject>
     */
    @GET
    suspend fun getTimetable(@Url url: String, @Query("email") email: String): List<Subject>

    /**
     * Delete thread
     * @param threadId String
     * @author tsnAnh
     */
    @DELETE("api/thread/{threadId}")
    suspend fun deleteThread(
        @Header("gg-auth-token") idToken: String,
        @Path("threadId") threadId: String
    )

    /**
     * Edit thread
     * @param threadId String
     * @author tsnAnh
     */
    @PUT("api/thread/{threadId}")
    suspend fun editThread(
        @Header("gg-auth-token") idToken: String,
        @Path("threadId") threadId: String
    )

    @GET("api/notification")
    suspend fun getNotifications(@Header("gg-auth-token") idToken: String): List<Notification>

    @PUT("/api/reply/like/{replyId}")
    suspend fun likeOrUnlikeReply(
        @Header("gg-auth-token") idToken: String,
        @Path("replyId") replyId: String
    ): List<String>

    /**
     * Get all news from daotao.sict.udn.vn
     * @param url String
     * @param time String
     * @return List<News>
     */
    @GET
    suspend fun getNews(@Url url: String, @Query("time") time: String): List<News>

    /**
     * Get all teacher from daotao.sict.udn.vn
     * @param url String
     * @return List<Teacher>
     */
    @GET
    suspend fun getAllTeachers(@Url url: String): List<Teacher>
}

/**
 * VKUServiceApi Singleton Object
 * @author tsnAnh
 */
object VKUServiceApi {
    val network: VKUService by lazy {
        retrofit.create(VKUService::class.java)
    }
}