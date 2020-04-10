package dev.tsnanh.vku.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

// Google Cloud VM Instance Server
const val BASE_URL = "http://34.87.13.195:3000"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
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

    @GET("user/{user_id}")
    suspend fun getUserProfileByUid(@Path("user_id") userId: String): NetworkUser

    @POST("user/new_user")
    suspend fun registerNewUser(@Header("Authorization") idToken: String): String

    @GET("forum/{forum_id}")
    suspend fun getForumById(@Path("forum_id") forumId: String): NetworkForum

    @GET("user/is_user_registered")
    suspend fun isUserRegistered(@Header("Authorization") idToken: String): Boolean
}

object VKUServiceApi {
    val network: VKUService by lazy {
        retrofit.create(VKUService::class.java)
    }
}