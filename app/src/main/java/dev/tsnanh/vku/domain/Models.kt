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
    var image: List<String> = emptyList(),
    var forumId: String = "",
    var numberOfPosts: Int = 1,
    var numberOfViews: Int = 0,
    var userId: String = "",
    var createAt: String = "",
    var lastUpdateOn: String = "",
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
    var userId: String = "",
    var threadId: String = "",
    var editHistory: List<String> = emptyList(),
    var createdAt: String = ""
)

fun ForumThread.asNetworkModel(): NetworkThread {
    return NetworkThread(
        id = id,
        title = title,
        image = image,
        forumId = forumId,
        numberOfPosts = numberOfPosts,
        numberOfViews = numberOfViews,
        userId = userId,
        createAt = createAt,
        lastUpdateOn = lastUpdateOn,
        posts = posts,
        editHistory = editHistory
    )
}

fun Post.asNetworkModel(): NetworkPost {
    return NetworkPost(
        id = id,
        content = content,
        userId = userId,
        threadId = threadId,
        editHistory = editHistory,
        createdAt = createdAt
    )
}