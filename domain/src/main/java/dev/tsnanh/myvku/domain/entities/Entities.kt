package dev.tsnanh.myvku.domain.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// A generic class that contains data and status about loading this data.
sealed class State<T>(
    val data: T? = null,
    val throwable: Throwable? = null,
) {
    class Success<T>(data: T) : State<T>(data)
    class Loading<T>(data: T? = null) : State<T>(data)
    class Error<T>(exception: Throwable, data: T? = null) : State<T>(data, throwable = exception)

    companion object {
        fun <T> loading(data: T? = null) = Loading(data)
        fun <T> error(exception: Throwable, data: T? = null) = Error(exception, data)
        fun <T> success(data: T) = Success(data)
    }
}

@Serializable
data class LoginBody(
    val tokenFCM: String
)

@Serializable
data class UpdateThreadBody(
    val title: String,
)

@Serializable
@Entity(tableName = "news")
data class News(
    @PrimaryKey
    @SerialName("CmsID")
    val cmsId: String,
    @SerialName("CategoryName")
    val categoryName: String?,
    @SerialName("Title")
    val title: String?,
    @SerialName("Content")
    val content: String?,
    @SerialName("CreatedDate")
    val createdDate: String?,
    @SerialName("UpdatedDate")
    val updatedDate: String?,
    @SerialName("Slug")
    val slug: String?,
    @SerialName("Attachment")
    val attachment: String?
)

@Entity(tableName = "subjects")
@Serializable
@Parcelize
data class Subject(
    @SerialName("masv")
    val studentCode: String,
    @SerialName("ten")
    val studentFullName: String,
    @SerialName("tenlop")
    @PrimaryKey
    val className: String,
    @SerialName("chucdanh")
    val title: String,
    @SerialName("hoten")
    val fullName: String,
    @SerialName("tuan")
    val week: String,
    @SerialName("thu")
    val dayOfWeek: String,
    @SerialName("tiet")
    val lesson: String,
    @SerialName("phong")
    val room: String
) : Parcelable

@Entity(tableName = "subjectNotifications")
data class SubjectNotification(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val className: String,
    val room: String,
    val title: String,
    val fullName: String,
    val week: String,
    val dayOfWeek: String,
    val lesson: String,
    val timeNotify: Long
)

@Serializable
data class Notification(
    @SerialName("_id")
    val id: String,
    val uid: String,
    val createdAt: Long,
    val message: NotificationMessageContainer,
    val hasSeen: Boolean
)

@Serializable
data class NotificationMessageContainer(
    val data: NotificationMessage
)

@Serializable
data class NotificationMessage(
    val uid: String,
    val userDisplayName: String,
    val title: String,
    val content: String,
    val photoURL: String?
)

enum class NotificationTitle(val value: String) {
    MESSAGE_LIKE("messageLike"),
    MESSAGE_TO_OWNER("messageToOwner"),
    MESSAGE_TO_QUOTED_USER("messageToQuotedUser"),
    MESSAGE_TO_OWNER_CUSTOM("messageToOwnerCustom"),
    MESSAGE_TO_ALL_SUBSCRIBERS("messageToAllSubscribers")
}

@Serializable
data class Teacher(
    @SerialName("hoten")
    val fullName: String,
    val phone: String,
    @SerialName("chucdanh")
    val title: String,
    val email: String,
    @SerialName("tendonvi")
    val unit: String
)

sealed class Notice

@Entity
@Serializable
data class Absence(
    @PrimaryKey
    @SerialName("tenlop")
    val className: String,
    @SerialName("chucdanh")
    val title: String,
    @SerialName("ten")
    val firstName: String,
    @SerialName("hodem")
    val lastName: String,
    @SerialName("ngaybaonghi")
    val dateNotice: String
) : Notice()

@Entity
@Serializable
data class MakeupClass(
    @PrimaryKey
    @SerialName("tenlop")
    val className: String,
    @SerialName("chucdanh")
    val title: String,
    @SerialName("hodem")
    val lastName: String,
    @SerialName("ten")
    val firstName: String,
    @SerialName("ngaybaobu")
    val dateMakeUp: String,
    @SerialName("tenphong")
    val room: String
) : Notice()