package dev.tsnanh.vku.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

const val BASE_URL = "http://35.197.136.211:3000"

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
        @Header("id_token") idToken: String,
        @Body container: NetworkCreateThreadContainer
    )

    @GET("t/{forum_id}")
    suspend fun getThreadsInForum(@Path("forum_id") forumId: String): NetworkThreadContainer

    @GET("user/{user_id}")
    suspend fun getUserById(@Path("user_id") userId: String): NetworkUser

    @POST("user/new_user")
    suspend fun registerNewUser(@Header("id_token") idToken: String)
}

object VKUServiceApi {
    val network: VKUService by lazy {
        retrofit.create(VKUService::class.java)
    }
}