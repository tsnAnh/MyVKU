/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.tsnanh.vku.database.DatabaseNews
import dev.tsnanh.vku.domain.*

/**
 * All Data class for object mapping
 */

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
data class NetworkClassroomContainer(
    val classrooms: List<NetworkClassroom>
)

@JsonClass(generateAdapter = true)
data class NetworkClassPostContainer(
    val classPosts: List<NetworkClassPost>
)

@JsonClass(generateAdapter = true)
data class NetworkCommentContainer(
    val comments: List<NetworkComment>
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
    @field:Json(name = "_id")
    var id: String = "",
    var title: String,
    @field:Json(name = "user_display_name")
    val userDisplayName: String = "",
    @field:Json(name = "user_avatar")
    var userAvatar: String = "",
    @field:Json(name = "forum_id")
    var forumId: String = "",
    @field:Json(name = "number_of_posts")
    var numberOfPosts: Int = 1,
    @field:Json(name = "number_of_views")
    var numberOfViews: Int = 0,
    @field:Json(name = "user_id")
    var userId: String = "",
    @field:Json(name = "created_at")
    var createdAt: Long = 0L,
    @field:Json(name = "last_updated_on")
    var lastUpdatedOn: Long = 0L,
    var posts: List<String> = emptyList(),
    @field:Json(name = "edit_history")
    var editHistory: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class NetworkUser(
    @field:Json(name = "_id")
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
    var posts: List<String>,
    var notifications: String,
    var role: Int = UserRole.STUDENT.value
)

@JsonClass(generateAdapter = true)
data class NetworkPost(
    @field:Json(name = "_id")
    var id: String = "",
    var content: String,
    var images: List<String> = emptyList(),
    @field:Json(name = "user_id")
    var userId: String = "",
    @field:Json(name = "user_display_name")
    var userDisplayName: String = "",
    @field:Json(name = "user_avatar")
    var userAvatar: String = "",
    @field:Json(name = "thread_id")
    var threadId: String = "",
    @field:Json(name = "thread_title")
    var threadTitle: String = "",
    @field:Json(name = "edit_history")
    var editHistory: List<String> = emptyList(),
    @field:Json(name = "created_at")
    var createdAt: Long = 0L,
    var quoted: String = "",
    @field:Json(name = "quoted_post")
    var quotedPost: NetworkPost? = null
)


data class NetworkNotification(
    @field:Json(name = "_id")
    val id: String,
    val user_id: String,
    @field:Json(name = "notification_objects")
    val notificationObjects: List<String>,
    @field:Json(name = "has_new_notification")
    val hasNewNotification: Boolean
)

data class NetworkNotificationObject(
    @field:Json(name = "_id")
    val id: String,
    @field:Json(name = "notification_id")
    val notificationId: String,
    @field:Json(name = "object")
    val mObject: Any,
    @field:Json(name = "notification_changes")
    val notificationChanges: List<String>,
    val status: Boolean
)

data class NetworkNotificationChange(
    @field:Json(name = "_id")
    val id: String,
    @field:Json(name = "notification_object_id")
    val notificationObjectId: String,
    val verb: String,
    val actor: String
)


data class NetworkClassroom(
    @field:Json(name = "_id")
    val id: String,
    val classname: String,
    val students: List<String>,
    @field:Json(name = "numberOfStudents")
    val numberOfStudents: Int,
    @field:Json(name = "homeroom_teacher")
    val homeroomTeacher: String
)

data class NetworkClassPost(
    @field:Json(name = "_id")
    val id: String,
    @field:Json(name = "class_id")
    val classId: String,
    @field:Json(name = "user_id")
    val userId: String,
    val likes: Int,
    @field:Json(name = "number_of_comments")
    val numberOfComments: Int,
    @field:Json(name = "number_of_shares")
    val numberOfShares: Int,
    @field:Json(name = "is_announcement")
    val isAnnouncement: Boolean,
    val pinned: Boolean,
    val content: String,
    val images: List<String>,
    @field:Json(name = "created_at")
    val createdAt: Long,
    @field:Json(name = "last_updated_on")
    val lastUpdatedOn: Long,
    val comments: List<String>,
    @field:Json(name = "edit_history")
    val editHistory: List<String>
)

data class NetworkComment(
    @field:Json(name = "_id")
    val id: String,
    @field:Json(name = "user_id")
    val userId: String,
    @field:Json(name = "class_post_id")
    val classPostId: String,
    @field:Json(name = "user_display_name")
    val userDisplayName: String,
    @field:Json(name = "user_avatar")
    val userAvatar: String,
    @field:Json(name = "edit_history")
    val editHistory: List<String>,
    val content: String,
    val images: List<String>,
    @field:Json(name = "created_at")
    val createdAt: String,
    @field:Json(name = "last_updated_on")
    val lastUpdatedOn: String
)

/**
 * Mapping function to domain object
 */

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

fun NetworkThreadContainer.asDomainModel(): List<ForumThread> {
    return threads.map {
        ForumThread(
            id = it.id,
            title = it.title,
            forumId = it.forumId,
            numberOfPosts = it.numberOfPosts,
            numberOfViews = it.numberOfViews,
            userId = it.userId,
            createdAt = it.createdAt,
            lastUpdatedOn = it.lastUpdatedOn,
            posts = it.posts,
            editHistory = it.editHistory,
            userAvatar = it.userAvatar,
            userDisplayName = it.userDisplayName
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
            posts = it.posts,
            notifications = it.notifications
        )
    }
}

fun NetworkPostContainer.asDomainModel(): List<Post> {
    return posts.map {
        Post(
            id = it.id,
            content = it.content,
            images = it.images,
            userId = it.userId,
            threadId = it.threadId,
            editHistory = it.editHistory,
            createdAt = it.createdAt,
            userDisplayName = it.userDisplayName,
            userAvatar = it.userAvatar,
            threadTitle = it.threadTitle,
            quoted = it.quoted,
            quotedPost = it.quotedPost?.asDomainModel()
        )
    }
}

fun NetworkClassroomContainer.asDomainModel(): List<Classroom> {
    return classrooms.map {
        Classroom(
            id = it.id,
            classname = it.classname,
            students = it.students,
            numberOfStudents = it.numberOfStudents,
            homeroomTeacher = it.homeroomTeacher
        )
    }
}

fun NetworkClassPostContainer.asDomainModel(): List<ClassPost> {
    return classPosts.map {
        ClassPost(
            id = it.id,
            classId = it.classId,
            userId = it.userId,
            likes = it.likes,
            numberOfComments = it.numberOfComments,
            numberOfShares = it.numberOfShares,
            isAnnouncement = it.isAnnouncement,
            content = it.content,
            images = it.images,
            createdAt = it.createdAt,
            lastUpdatedOn = it.lastUpdatedOn,
            comments = it.comments,
            editHistory = it.editHistory,
            pinned = it.pinned
        )
    }
}

fun NetworkCommentContainer.asDomainModel(): List<Comment> {
    return comments.map {
        Comment(
            id = it.id,
            userId = it.userId,
            classPostId = it.classPostId,
            userDisplayName = it.userDisplayName,
            userAvatar = it.userAvatar,
            editHistory = it.editHistory,
            content = it.content,
            images = it.images,
            createdAt = it.createdAt,
            lastUpdatedOn = it.lastUpdatedOn
        )
    }
}

fun NetworkForum.asDomainModel(): Forum {
    return Forum(
        id,
        title,
        subtitle,
        description,
        image,
        numberOfThreads,
        numberOfPosts,
        lastUpdatedOn,
        threads
    )
}

fun NetworkThread.asDomainModel(): ForumThread {
    return ForumThread(
        id,
        title,
        userAvatar,
        userDisplayName,
        forumId,
        numberOfPosts,
        numberOfViews,
        userId,
        createdAt,
        lastUpdatedOn,
        posts,
        editHistory
    )
}

fun NetworkPost.asDomainModel(): Post {
    return Post(
        id,
        content,
        images,
        userId,
        userAvatar,
        threadId,
        threadTitle,
        editHistory,
        createdAt,
        userDisplayName,
        quoted,
        quotedPost?.asDomainModel()
    )
}

fun NetworkNotification.asDomainModel(): Notification {
    return Notification(
        id, user_id, notificationObjects, hasNewNotification
    )
}

fun NetworkNotificationObject.asDomainModel(): NotificationObject {
    return NotificationObject(
        id, notificationId, mObject, notificationChanges, status
    )
}

fun NetworkNotificationChange.asDomainModel(): NotificationChange {
    return NotificationChange(
        id, notificationObjectId, verb, actor
    )
}

fun NetworkClassroom.asDomainModel() = Classroom(
    id, classname, students, numberOfStudents, homeroomTeacher
)

fun NetworkClassPost.asDomainModel() = ClassPost(
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

fun NetworkComment.asDomainModel() = Comment(
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