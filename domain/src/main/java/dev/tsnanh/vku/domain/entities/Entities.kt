package dev.tsnanh.vku.domain.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

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
    val lastUpdatedAt: String
)

@JsonClass(generateAdapter = true)
data class ForumThread(
    @field:Json(name = "_id")
    val id: String = "",
    val uid: String = "",
    val title: String,
    val forumId: String = "",
    val numberOfReplies: Int = 1,
    val numberOfViews: Int = 0,
    val likes: List<String> = emptyList(),
    val createdAt: Long = 0L,
    val lastUpdatedAt: Long = 0L/*,
    val editHistory: List<String> = emptyList()*/
)

@JsonClass(generateAdapter = true)
data class User(
    @field:Json(name = "_id")
    val id: String = "",
    val uidGG: String,
    val displayName: String,
    val photoURL: String,
    val email: String,
    val role: Int = 0
)

@JsonClass(generateAdapter = true)
data class Reply(
    @field:Json(name = "_id")
    val id: String = "",
    val uid: String = "",
    val content: String,
    var images: List<String> = emptyList(),
    val threadId: String = "",
    val forumId: String = "",
    val editHistory: List<String> = emptyList(),
    val createdAt: Long = 0L,
    val quoted: String = "",
    val quotedReply: Reply? = null
)

@Entity(tableName = "subjects")
@JsonClass(generateAdapter = true)
@Parcelize
data class Subject(
    @field:Json(name = "tenlop")
    @PrimaryKey
    val className: String,
    @field:Json(name = "chucdanh")
    val title: String,
    @field:Json(name = "hoten")
    val fullName: String,
    @field: Json(name = "tuan")
    val week: String,
    @field:Json(name = "thu")
    val dayOfWeek: String,
    @field:Json(name = "tiet")
    val lesson: String,
    @field:Json(name = "phong")
    val room: String
) : Parcelable