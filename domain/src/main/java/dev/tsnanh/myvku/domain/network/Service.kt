/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */
package dev.tsnanh.myvku.domain.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.tsnanh.myvku.domain.constants.SecretConstants
import dev.tsnanh.myvku.domain.entities.Absence
import dev.tsnanh.myvku.domain.entities.MakeupClass
import dev.tsnanh.myvku.domain.entities.News
import dev.tsnanh.myvku.domain.entities.Notification
import dev.tsnanh.myvku.domain.entities.Subject
import dev.tsnanh.myvku.domain.entities.Teacher
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

/**
 * Base URL
 */
const val BASE_URL = "http://13.229.109.199:8008"

/**
 * OkHttp client
 */
private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

/**
 * Retrofit client
 */
@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(
        Json {
            isLenient = true
        }.asConverterFactory(MediaType.parse("application/json")!!)
    )
    .baseUrl(BASE_URL)
    .build()

interface VKUService {
    /**
     * Retrieve user's timetable
     * @author tsnAnh
     * @param email String
     * @return subjects List<Subject>
     */
    @GET
    suspend fun getTimetable(
        @Url url: String = SecretConstants.TKB_URL,
        @Query("email") email: String
    ): List<Subject>

    @GET("api/notification")
    suspend fun getNotifications(@Header("gg-auth-token") idToken: String): List<Notification>

    /**
     * Get all news from daotao.sict.udn.vn
     * @param url String
     * @param time String
     * @return List<News>
     */
    @GET
    suspend fun getNews(
        @Url url: String = SecretConstants.NEWS_URL,
        @Query("time") time: String = ""
    ): List<News>

    /**
     * Get all teacher from daotao.sict.udn.vn
     * @param url String
     * @return List<Teacher>
     */
    @GET
    suspend fun getAllTeachers(@Url url: String = SecretConstants.TEACHERS_URL): List<Teacher>

    @GET
    suspend fun getAbsenceNotice(
        @Url url: String = SecretConstants.ABSENCE_URL,
        @Query("time") time: String = ""
    ): List<Absence>

    @GET
    suspend fun getMakeUpClassNotice(
        @Url url: String = SecretConstants.MAKEUP_URL,
        @Query("time") time: String = ""
    ): List<MakeupClass>
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