/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */
package dev.tsnanh.vku.domain.network

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.tsnanh.vku.domain.entities.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

/**
 * Base URL
 */
// Google Cloud VM Instance Server
const val BASE_URL = "http://34.87.151.214:3000/"

/**
 * OkHttp client
 */
private val client = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
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

private val sharedPreferences by inject(SharedPreferences::class.java)

interface VKUService {

    @GET("n")
    suspend fun getLatestNews(): NewsContainer

    @GET("f")
    suspend fun getForums(): ForumContainer

    @POST("t/create")
    suspend fun createThread(
        @Header("Authorization") idToken: String,
        @Body container: CreateThreadContainer
    ): ForumThread

    @GET("t/{forumId}")
    suspend fun getThreads(@Path("forumId") forumId: String): ThreadContainer

    @GET("u/get/{userId}")
    suspend fun getUserById(@Path("userId") userId: String): User

    @GET("f/get/{forumId}")
    suspend fun getForumById(@Path("forumId") forumId: String): Forum

    @GET("p/{threadId}")
    suspend fun getRepliesInThread(
        @Path("threadId") threadId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ReplyContainer

    @GET("t/get/{threadId}")
    suspend fun getThreadById(@Path("threadId") threadId: String): Thread

    @Multipart
    @POST("/p/upload/{uid}")
    suspend fun uploadImage(
        @Header("Authorization") idToken: String,
        @Path("uid") uid: String,
        @Part image: MultipartBody.Part
    ): String

    @POST("/p/new")
    suspend fun newReply(
        @Header("Authorization") idToken: String,
        @Body post: Reply
    ): Reply

    @GET("/p/get/{id}")
    suspend fun getReplyById(@Path("id") id: String): Reply

    @POST("/u/has-user")
    suspend fun hasUser(@Body userId: TokenRequestContainer): HasUserResponse

    @POST("/u/sign-up")
    suspend fun signUp(@Body idToken: String): User
}

/**
 * VKUServiceApi Singleton Object
 */
object VKUServiceApi {
    val network: VKUService by lazy {
        retrofit.create(VKUService::class.java)
    }
}