/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

// Google Cloud VM Instance Server
const val BASE_URL = "http://34.87.13.195:3000"

private val client = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface VKUService {
    @GET("news")
    suspend fun getLatestNews(): NetworkNewsContainer

    @GET("forum")
    suspend fun getAllSubForums(): NetworkForumContainer

    @POST("t/create")
    suspend fun createThread(
        @Header("Authorization") idToken: String,
        @Body container: NetworkCreateThreadContainer
    ): NetworkThread

    @GET("t/{forum_id}")
    suspend fun getThreadsInForum(@Path("forum_id") forumId: String): NetworkThreadContainer

    @GET("user/get/{user_id}")
    suspend fun getUserProfileByUid(@Path("user_id") userId: String): NetworkUser

    @POST("user/new_user")
    suspend fun registerNewUser(@Header("Authorization") idToken: String): String

    @GET("forum/get/{forum_id}")
    suspend fun getForumById(@Path("forum_id") forumId: String): NetworkForum

    @GET("user/is_user_registered")
    suspend fun isUserRegistered(@Header("Authorization") idToken: String): Boolean

    @GET("r/{thread_id}")
    suspend fun getRepliesInThread(@Path("thread_id") threadId: String): NetworkPostContainer

    @GET("t/get/{thread_id}")
    suspend fun getThreadById(@Path("thread_id") threadId: String): NetworkThread

    @Multipart
    @POST("/r/upload/{uid}")
    suspend fun uploadImage(
        @Header("Authorization") idToken: String,
        @Path("uid") uid: String,
        @Part image: MultipartBody.Part
    ): String

    @POST("/r/new")
    suspend fun newReply(
        @Header("Authorization") idToken: String,
        @Body post: NetworkPost
    ): NetworkPost

    @GET("/r/get/{id}")
    suspend fun getReplyById(@Path("id") id: String): NetworkPost
}

object VKUServiceApi {
    val network: VKUService by lazy {
        retrofit.create(VKUService::class.java)
    }
}