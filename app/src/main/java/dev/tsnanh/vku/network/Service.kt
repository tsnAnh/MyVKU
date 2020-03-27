package dev.tsnanh.vku.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://4bd577c9.ngrok.io"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface VKUService {
    @GET("news")
    suspend fun getLatestNews(): NetworkNewsContainer

    @GET("forum")
    suspend fun getAllSubForums(): NetworkForumContainer
}

object VKUServiceApi {
    val network: VKUService by lazy {
        retrofit.create(VKUService::class.java)
    }
}