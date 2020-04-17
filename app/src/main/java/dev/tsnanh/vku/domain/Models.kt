/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.domain

import dev.tsnanh.vku.network.NetworkPost
import dev.tsnanh.vku.network.NetworkThread

/**
 * Models.kt
 * @author tsnAnh
 */

/**
 * Domain News Data Class
 */
data class News(
    var url: String,
    var title: String,
    var date: String,
    var category: String
)

/**
 * Domain Forum Data Class
 */
data class Forum(
    var id: String = "",
    var title: String,
    var subtitle: String,
    var description: String,
    var image: String,
    var numberOfThreads: Int,
    var numberOfPosts: Int,
    var lastUpdatedOn: String,
    var threads: List<String>
)

/**
 * Domain Thread Data Class
 */
data class ForumThread(
    var id: String = "",
    var title: String,
    var userAvatar: String = "",
    val userDisplayName: String = "",
    var forumId: String = "",
    var numberOfPosts: Int = 1,
    var numberOfViews: Int = 0,
    var userId: String = "",
    var createdAt: Long = 0L,
    var lastUpdatedOn: Long = 0L,
    var posts: List<String> = emptyList(),
    var editHistory: List<String> = emptyList()
)

/**
 * Domain User Data Class
 */
data class User(
    var id: String = "",
    var uid: String,
    var displayName: String,
    var photoUrl: String,
    var email: String,
    var isEmailVerified: Boolean,
    var providerId: String,
    var numberOfThreads: String,
    var threads: List<String>,
    var posts: List<String>
)

/**
 * Domain Post Data Class
 */
data class Post(
    var id: String = "",
    var content: String,
    var images: List<String> = emptyList(),
    var userId: String = "",
    var userAvatar: String = "",
    var threadId: String = "",
    var threadTitle: String = "",
    var editHistory: List<String> = emptyList(),
    var createdAt: Long = 0L,
    val userDisplayName: String = ""
)

fun ForumThread.asNetworkModel(): NetworkThread {
    return NetworkThread(
        id = id,
        title = title,
        forumId = forumId,
        numberOfPosts = numberOfPosts,
        numberOfViews = numberOfViews,
        userId = userId,
        createdAt = createdAt,
        lastUpdatedOn = lastUpdatedOn,
        posts = posts,
        editHistory = editHistory,
        userAvatar = userAvatar,
        userDisplayName = userDisplayName
    )
}

fun Post.asNetworkModel(): NetworkPost {
    return NetworkPost(
        id = id,
        content = content,
        images = images,
        userId = userId,
        threadId = threadId,
        editHistory = editHistory,
        createdAt = createdAt,
        threadTitle = threadTitle,
        userAvatar = userAvatar,
        userDisplayName = userDisplayName
    )
}

// A generic class that contains data and status about loading this data.
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}
