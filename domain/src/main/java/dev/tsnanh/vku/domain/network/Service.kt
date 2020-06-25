/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */
package dev.tsnanh.vku.domain.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.tsnanh.vku.domain.entities.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

/**
 * Base URL
 */
// Google Cloud VM Instance Server
const val BASE_URL = "http://34.87.151.214:5000"

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

    // region News routes
    /**
     * Get latest news from REST API
     * @method GET
     * @return news NewsContainer
     */
    @GET("n")
    suspend fun getLatestNews(): NewsContainer
    // endregion

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
     * @param container CreateThreadContainer
     * @param forumId String
     * @method POST
     * @return thread ForumThread
     */
    @POST("api/thread/{forumId}")
    suspend fun createThread(
        @Header("gg-auth-token") idToken: String,
        @Body container: CreateThreadContainer,
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
     * @return user User
     */
    @GET("u/get/{userId}")
    suspend fun getUserById(@Path("userId") userId: String): User

    /**
     * Get all replies in specified thread
     * @param threadId String, page Int, limit Int
     * @method GET
     * @return replyContainer ReplyContainer
     */
    @GET("api/thread/reply/{threadId}")
    suspend fun getRepliesInThread(
        @Path("threadId") threadId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ReplyContainer

    @GET("api/thread/{threadId}")
    suspend fun getThreadById(@Path("threadId") threadId: String): Thread

    @Multipart
    @POST("/p/upload/{uid}")
    suspend fun uploadImage(
        @Header("gg-auth-token") idToken: String,
        @Path("uid") uid: String,
        @Part image: MultipartBody.Part
    ): String

    @POST("/p/new")
    suspend fun newReply(
        @Header("gg-auth-token") idToken: String,
        @Body reply: Reply
    ): Reply

    @GET("/p/get/{id}")
    suspend fun getReplyById(@Path("id") id: String): Reply

    /**
     * Login
     * @param idToken String
     * @return response LoginResponse
     */
    @GET("api/user/auth")
    suspend fun login(@Header("gg-auth-token") idToken: String): LoginResponse

    /**
     * Retrieve user's timetable
     * @param email String
     * @return subjects List<Subject>
     */
    @GET
    suspend fun getTimetable(@Url url: String, @Query("email") email: String): List<Subject>

    /**
     * Delete thread
     * @param threadId String
     */
    @DELETE("api/thread/{threadId}")
    suspend fun deleteThread(
        @Header("gg-auth-token") idToken: String,
        @Path("threadId") threadId: String
    )

    /**
     * Edit thread
     * @param threadId String
     */
    @PUT("api/thread/{threadId}")
    suspend fun editThread(
        @Header("gg-auth-token") idToken: String,
        @Path("threadId") threadId: String
    )
}

/**
 * VKUServiceApi Singleton Object
 */
object VKUServiceApi {
    val network: VKUService by lazy {
        retrofit.create(VKUService::class.java)
    }
}