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
    val throwable: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(exception: Throwable, data: T? = null) : Resource<T>(data, throwable = exception)
}

sealed class WorkResult<out T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : WorkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : WorkResult<T>(data, message)
}

@JsonClass(generateAdapter = true)
data class LoginBody(
    val tokenFCM: String
)

@JsonClass(generateAdapter = true)
data class UpdateThreadBody(
    val title: String,
)

@JsonClass(generateAdapter = true)
@Entity(tableName = "news")
data class News(
    @PrimaryKey
    @field:Json(name = "CmsID")
    val cmsId: String,
    @field:Json(name = "CategoryName")
    val categoryName: String?,
    @field:Json(name = "Title")
    val title: String?,
    @field:Json(name = "Content")
    val content: String?,
    @field:Json(name = "CreatedDate")
    val createdDate: String?,
    @field:Json(name = "UpdatedDate")
    val updatedDate: String?,
    @field:Json(name = "Slug")
    val slug: String?,
    @field:Json(name = "Attachment")
    val attachment: String?
)

@Entity(tableName = "subjects")
@JsonClass(generateAdapter = true)
@Parcelize
data class Subject(
    @field:Json(name = "masv")
    val studentCode: String,
    @field:Json(name = "ten")
    val studentFullName: String,
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

@JsonClass(generateAdapter = true)
data class Notification(
    @field:Json(name = "_id")
    val id: String,
    val uid: String,
    val createdAt: Long,
    val message: NotificationMessageContainer,
    val hasSeen: Boolean
)

@JsonClass(generateAdapter = true)
data class NotificationMessageContainer(
    val data: NotificationMessage
)

@JsonClass(generateAdapter = true)
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

@JsonClass(generateAdapter = true)
data class Teacher(
    @field:Json(name = "hoten")
    val fullName: String,
    val phone: String,
    @field:Json(name = "chucdanh")
    val title: String,
    val email: String,
    @field:Json(name = "tendonvi")
    val unit: String
)

sealed class Notice

//@Entity
@JsonClass(generateAdapter = true)
data class Absence(
    @field:Json(name = "tenlop")
    val className: String,
    @field:Json(name = "chucdanh")
    val title: String,
    @field:Json(name = "ten")
    val firstName: String,
    @field:Json(name = "hodem")
    val lastName: String,
    @field:Json(name = "ngaybaonghi")
    val dateNotice: String
) : Notice()

//@Entity
@JsonClass(generateAdapter = true)
data class MakeUpClass(
    @field:Json(name = "tenlop")
    val className: String,
    @field:Json(name = "chucdanh")
    val title: String,
    @field:Json(name = "hodem")
    val lastName: String,
    @field:Json(name = "ten")
    val firstName: String,
    @field:Json(name = "ngaybaobu")
    val dateMakeUp: String,
    @field:Json(name = "tenphong")
    val room: String
) : Notice()