/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.domain

import dev.tsnanh.vku.network.*

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
    var posts: List<String>,
    var notifications: String,
    var role: Int = UserRole.STUDENT.value
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
    var userDisplayName: String = "",
    var quoted: String = "",
    var quotedPost: Post? = null
)

data class Notification(
    val id: String,
    val user_id: String,
    val notification_objects: List<String>,
    val hasNewNotification: Boolean
)

data class NotificationObject(
    val id: String,
    val notification_id: String,
    val mObject: Any,
    val notificationChanges: List<String>,
    val status: Boolean
)

data class NotificationChange(
    val id: String,
    val notificationObjectId: String,
    val verb: String,
    val actor: String
)

data class Classroom(
    val id: String,
    val classname: String,
    val students: List<String>,
    val numberOfStudents: Int,
    val homeroomTeacher: String
)

data class ClassPost(
    val id: String,
    val classId: String,
    val userId: String,
    val likes: Int,
    val numberOfComments: Int,
    val numberOfShares: Int,
    val isAnnouncement: Boolean,
    val pinned: Boolean,
    val content: String,
    val images: List<String>,
    val createdAt: Long,
    val lastUpdatedOn: Long,
    val comments: List<String>,
    val editHistory: List<String>
)

data class Comment(
    val id: String,
    val userId: String,
    val classPostId: String,
    val userDisplayName: String,
    val userAvatar: String,
    val editHistory: List<String>,
    val content: String,
    val images: List<String>,
    val createdAt: String,
    val lastUpdatedOn: String
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
        userDisplayName = userDisplayName,
        quoted = quoted,
        quotedPost = quotedPost?.asNetworkModel()
    )
}

fun Notification.asNetworkModel(): NetworkNotification {
    return NetworkNotification(
        id, user_id, notification_objects, hasNewNotification
    )
}

fun NotificationObject.asNetworkModel(): NetworkNotificationObject {
    return NetworkNotificationObject(
        id, notification_id, mObject, notificationChanges, status
    )
}

fun NotificationChange.asNetworkModel(): NetworkNotificationChange {
    return NetworkNotificationChange(
        id, notificationObjectId, verb, actor
    )
}

fun Classroom.asNetworkModel(): NetworkClassroom {
    return NetworkClassroom(
        id, classname, students, numberOfStudents, homeroomTeacher
    )
}

fun ClassPost.asNetworkModel(): NetworkClassPost {
    return NetworkClassPost(
        id,
        classId,
        userId,
        likes,
        numberOfComments,
        numberOfShares,
        isAnnouncement,
        pinned,
        content,
        images,
        createdAt,
        lastUpdatedOn,
        comments,
        editHistory
    )
}

fun Comment.asNetworkModel(): NetworkComment {
    return NetworkComment(
        id,
        userId,
        classPostId,
        userDisplayName,
        userAvatar,
        editHistory,
        content,
        images,
        createdAt,
        lastUpdatedOn
    )
}

// A generic class that contains data and status about loading this data.
sealed class Resource<out T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}

enum class UserRole(val value: Int) {
    STUDENT(0),
    TEACHER(1),
    HOMEROOM_TEACHER(2),
    ADMIN(100)
}