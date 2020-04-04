package dev.tsnanh.vku.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.tsnanh.vku.database.*
import dev.tsnanh.vku.domain.*

@JsonClass(generateAdapter = true)
data class NetworkNewsContainer(
    val news: List<NetworkNews>
)

@JsonClass(generateAdapter = true)
data class NetworkForumContainer(
    val forums: List<NetworkForum>
)

@JsonClass(generateAdapter = true)
data class NetworkThreadContainer(
    val threads: List<NetworkThread>
)

@JsonClass(generateAdapter = true)
data class NetworkUserContainer(
    val users: List<NetworkUser>
)

@JsonClass(generateAdapter = true)
data class NetworkPostContainer(
    val posts: List<NetworkPost>
)

@JsonClass(generateAdapter = true)
data class NetworkCreateThreadContainer(
    val thread: NetworkThread,
    val post: NetworkPost
)

@JsonClass(generateAdapter = true)
data class NetworkNews(
    val url: String,
    val title: String,
    val category: String,
    val date: String
)

@JsonClass(generateAdapter = true)
data class NetworkForum(
    @field:Json(name = "_id")
    var id: String = "",
    var title: String,
    var subtitle: String,
    var description: String,
    var image: String,
    @field:Json(name = "number_of_threads")
    var numberOfThreads: Int,
    @field:Json(name = "number_of_posts")
    var numberOfPosts: Int,
    @field:Json(name = "last_updated_on")
    var lastUpdatedOn: String,
    var threads: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class NetworkThread(
    var id: String = "",
    var title: String,
    var image: List<String> = emptyList(),
    @field:Json(name = "forum_id")
    var forumId: String = "",
    @field:Json(name = "number_of_posts")
    var numberOfPosts: Int = 1,
    @field:Json(name = "number_of_views")
    var numberOfViews: Int = 0,
    @field:Json(name = "user_id")
    var userId: String = "",
    @field:Json(name = "created_at")
    var createAt: String = "",
    @field:Json(name = "last_update_on")
    var lastUpdateOn: String = "",
    var posts: List<String> = emptyList(),
    @field:Json(name = "edit_history")
    var editHistory: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class NetworkUser(
    var id: String = "",
    var uid: String,
    @field:Json(name = "display_name")
    var displayName: String,
    @field:Json(name = "photo_url")
    var photoUrl: String,
    var email: String,
    @field:Json(name = "is_email_verified")
    var isEmailVerified: Boolean,
    @field:Json(name = "provider_id")
    var providerId: String,
    @field:Json(name = "number_of_threads")
    var numberOfThreads: String,
    var threads: List<String>,
    var posts: List<String>
)

@JsonClass(generateAdapter = true)
data class NetworkPost(
    var id: String = "",
    var content: String,
    @field:Json(name = "user_id")
    var userId: String = "",
    @field:Json(name = "thread_id")
    var threadId: String = "",
    @field:Json(name = "edit_history")
    var editHistory: List<String> = emptyList(),
    @field:Json(name = "created_at")
    var createdAt: String = ""
)

fun NetworkNewsContainer.asDomainModel(): List<News> {
    return news.map {
        News(
            url = it.url,
            title = it.title,
            date = it.date,
            category = it.category
        )
    }
}

fun NetworkNewsContainer.asDatabaseModel(): Array<DatabaseNews> {
    return news.map {
        DatabaseNews(
            url = it.url,
            title = it.title,
            date = it.date,
            category = it.category
        )
    }.toTypedArray()
}

fun NetworkForumContainer.asDomainModel(): List<Forum> {
    return forums.map {
        Forum(
            id = it.id,
            title = it.title,
            subtitle = it.subtitle,
            description = it.description,
            image = it.image,
            numberOfThreads = it.numberOfThreads,
            numberOfPosts = it.numberOfPosts,
            lastUpdatedOn = it.lastUpdatedOn,
            threads = it.threads
        )
    }
}

fun NetworkThreadContainer.asDomainModel(): List<dev.tsnanh.vku.domain.ForumThread> {
    return threads.map {
        ForumThread(
            id = it.id,
            title = it.title,
            image = it.image,
            forumId = it.forumId,
            numberOfPosts = it.numberOfPosts,
            numberOfViews = it.numberOfViews,
            userId = it.userId,
            createAt = it.createAt,
            lastUpdateOn = it.lastUpdateOn,
            posts = it.posts,
            editHistory = it.editHistory
        )
    }
}

fun NetworkUserContainer.asDomainModel(): List<User> {
    return users.map {
        User(
            id = it.id,
            uid = it.uid,
            displayName = it.displayName,
            photoUrl = it.photoUrl,
            email = it.email,
            isEmailVerified = it.isEmailVerified,
            providerId = it.providerId,
            numberOfThreads = it.numberOfThreads,
            threads = it.threads,
            posts = it.posts
        )
    }
}

fun NetworkPostContainer.asDomainModel(): List<Post> {
    return posts.map {
        Post(
            id = it.id,
            content = it.content,
            userId = it.userId,
            threadId = it.threadId,
            editHistory = it.editHistory,
            createdAt = it.createdAt
        )
    }
}