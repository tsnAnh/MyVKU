package dev.tsnanh.vku.network

import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

const val BASE_URL = "http://4a7109b2.ngrok.io"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface VKUService {
    @GET("news")
    suspend fun getLatestNews(): NetworkNewsContainer

    @GET("forum")
    suspend fun getAllSubForums(): NetworkForumContainer

    @Multipart
    @POST("thread/create")
    suspend fun createThread(
        @Header("id_token") idToken: String,
        @Body container: NetworkCreateThreadContainer,
        @Part images: List<MultipartBody.Part>
    )
}

object VKUServiceApi {
    val network: VKUService by lazy {
        retrofit.create(VKUService::class.java)
    }
}