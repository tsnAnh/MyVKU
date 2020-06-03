package dev.tsnanh.vku.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// A generic class that contains data and status about loading this data.
sealed class Resource<out T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}

@JsonClass(generateAdapter = true)
data class TokenRequestContainer(
    val token: String
)

@JsonClass(generateAdapter = true)
data class HasUserResponse(
    val user: User,
    val isNew: Boolean
)

@JsonClass(generateAdapter = true)
data class NewsContainer(
    val news: List<News>
)

@JsonClass(generateAdapter = true)
data class ForumContainer(
    val forums: List<Forum>
)

@JsonClass(generateAdapter = true)
data class ThreadContainer(
    val threads: List<ForumThread>
)

@JsonClass(generateAdapter = true)
data class UserContainer(
    val users: List<User>
)

@JsonClass(generateAdapter = true)
data class ReplyContainer(
    val replies: List<Reply>,
    val totalPages: Int,
    val currentPage: Int
)

@JsonClass(generateAdapter = true)
data class CreateThreadContainer(
    val thread: ForumThread,
    val reply: Reply
)

@JsonClass(generateAdapter = true)
data class ClassroomContainer(
    val classrooms: List<Classroom>
)

@JsonClass(generateAdapter = true)
data class ClassPostContainer(
    val classPosts: List<ClassPost>
)

@JsonClass(generateAdapter = true)
data class CommentContainer(
    val comments: List<Comment>
)

@JsonClass(generateAdapter = true)
@Entity(tableName = "news")
data class News(
    @PrimaryKey
    val url: String,
    val title: String,
    val category: String,
    val date: String
)

@JsonClass(generateAdapter = true)
data class Forum(
    @field:Json(name = "_id")
    val id: String = "",
    val title: String,
    val subtitle: String,
    val description: String,
    val image: String,
    val numberOfThreads: Int,
    val numberOfReplies: Int,
    val lastUpdatedOn: String,
    val threads: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class ForumThread(
    @field:Json(name = "_id")
    val id: String = "",
    val title: String,
    val userDisplayName: String = "",
    val userAvatar: String = "",
    val forumId: String = "",
    val numberOfReplies: Int = 1,
    val numberOfViews: Int = 0,
    val userId: String = "",
    val createdAt: Long = 0L,
    val lastUpdatedOn: Long = 0L,
    val replies: List<String> = emptyList(),
    val editHistory: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class User(
    @field:Json(name = "_id")
    val id: String = "",
    val uid: String,
    val displayName: String,
    val photoUrl: String,
    val email: String,
    val emailVerified: Boolean,
    val numberOfThreads: String,
    val threads: List<String>,
    val replies: List<String>
)

@JsonClass(generateAdapter = true)
data class Reply(
    @field:Json(name = "_id")
    val id: String = "",
    val content: String,
    var images: List<String> = emptyList(),
    val userId: String = "",
    val userDisplayName: String = "",
    val userAvatar: String = "",
    val threadId: String = "",
    val threadTitle: String = "",
    val editHistory: List<String> = emptyList(),
    val createdAt: Long = 0L,
    val quoted: String = "",
    val quotedReply: Reply? = null
)

data class Classroom(
    @field:Json(name = "_id")
    val id: String,
    val classname: String,
    val students: List<String>,
    val numberOfStudents: Int,
    val homeroomTeacher: String
)

data class ClassPost(
    @field:Json(name = "_id")
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
    @field:Json(name = "_id")
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
