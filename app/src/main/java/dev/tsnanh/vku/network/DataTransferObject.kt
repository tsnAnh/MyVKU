package dev.tsnanh.vku.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.tsnanh.vku.database.DatabaseForum
import dev.tsnanh.vku.database.DatabaseNews
import dev.tsnanh.vku.domain.News

@JsonClass(generateAdapter = true)
data class NetworkNewsContainer(
    val news: List<NetworkNews>
)

@JsonClass(generateAdapter = true)
data class NetworkForumContainer(
    val forums: List<NetworkForum>
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
    var id: String,
    var title: String,
    var subtitle: String,
    var description: String,
    var image: String,
    @field:Json(name = "number_thread")
    var numberThread: Int,
    @field:Json(name = "number_post")
    var numberPost: Int,
    @field:Json(name = "last_updated_on")
    var lastUpdatedOn: String
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

fun NetworkForumContainer.asDatabaseModel(): Array<DatabaseForum> {
    return forums.map {
        DatabaseForum(
            id = it.id,
            title = it.title,
            subtitle = it.subtitle,
            description = it.description,
            image = it.image,
            numberThread = it.numberThread,
            numberPost = it.numberPost,
            lastUpdatedOn = it.lastUpdatedOn
        )
    }.toTypedArray()
}