package dev.tsnanh.vku.domain.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
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
    val replies: List<NetworkCustomReply>,
    val totalPages: Int,
    val currentPage: Int
)

@JsonClass(generateAdapter = true)
data class CreateThreadContainer(
    val thread: ForumThread,
    val reply: Reply
)