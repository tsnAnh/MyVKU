package dev.tsnanh.vku.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "user")
@JsonClass(generateAdapter = true)
data class User(
    @PrimaryKey
    @field:Json(name = "_id")
    val id: String = "",
    val uidGG: String,
    val displayName: String,
    val photoURL: String,
    val email: String,
    val role: Int = 0,
)

@JsonClass(generateAdapter = true)
data class Forum(
    @PrimaryKey
    @field:Json(name = "_id")
    val id: String = "",
    val title: String,
    val numberOfThreads: Int,
    val numberOfReplies: Int,
    val latestThread: String?,
    val tag: String,
)

@JsonClass(generateAdapter = true)
data class NetworkForum(
    @PrimaryKey
    @field:Json(name = "_id")
    val id: String = "",
    val title: String,
    val numberOfThreads: Int,
    val numberOfReplies: Int,
    val latestThread: ForumThread?,
    val tag: String,
)

@Entity(tableName = "thread")
@JsonClass(generateAdapter = true)
data class ForumThread(
    @PrimaryKey
    @field:Json(name = "_id")
    val id: String = "",
    val uid: String = "",
    val title: String,
    val forumId: String = "",
    val numberOfReplies: Int = 1,
    val numberOfViews: Int = 1,
    val likes: List<String> = emptyList(),
    val createdAt: Long = 0L,
    val latestReply: String? = null,
)

@JsonClass(generateAdapter = true)
data class NetworkForumThread(
    @field:Json(name = "_id")
    val id: String = "",
    val uid: User,
    val title: String,
    val forumId: String = "",
    val numberOfReplies: Int = 1,
    val numberOfViews: Int = 1,
    val likes: List<String> = emptyList(),
    val createdAt: Long = 0L,
    val latestReply: Reply?,
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
    val quoted: UnpopulatedQuotedReply? = null,
)

@JsonClass(generateAdapter = true)
data class NetworkReply(
    @field:Json(name = "_id")
    val id: String = "",
    val uid: User? = null,
    val content: String,
    var images: List<String> = emptyList(),
    val threadId: String = "",
    val forumId: String = "",
    val editHistory: List<String> = emptyList(),
    val createdAt: Long = 0L,
    val quoted: PopulatedQuotedReply? = null,
)

@JsonClass(generateAdapter = true)
data class UnpopulatedQuotedReply(
    val replyId: String,
    val isDeleted: Boolean? = null,
)

@JsonClass(generateAdapter = true)
data class PopulatedQuotedReply(
    val replyId: NetworkReply?,
    val isDeleted: Boolean? = null,
)