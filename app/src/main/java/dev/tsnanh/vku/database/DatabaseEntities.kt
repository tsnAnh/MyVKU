package dev.tsnanh.vku.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.tsnanh.vku.domain.Forum
import dev.tsnanh.vku.domain.News

@Entity(tableName = "news")
data class DatabaseNews(
    @PrimaryKey
    var url: String,
    var title: String,
    var category: String,
    var date: String
)

@Entity(tableName = "forums")
data class DatabaseForum(
    @PrimaryKey
    var id: String,
    var title: String,
    var subtitle: String,
    var description: String,
    var image: String,
    @ColumnInfo(name = "number_thread")
    var numberThread: Int,
    @ColumnInfo(name = "number_post")
    var numberPost: Int,
    @ColumnInfo(name = "last_updated_on")
    var lastUpdatedOn: String
)

@JvmName(name = "ListDatabaseNewsAsDomainModel")
fun List<DatabaseNews>.asDomainModel(): List<News> {
    return map {
        News(
            url = it.url,
            title = it.title,
            category = it.category,
            date = it.date
        )
    }
}

@JvmName(name = "ListDatabaseForumAsDomainModel")
fun List<DatabaseForum>.asDomainModel(): List<Forum> {
    return map {
        Forum(
            id = it.id,
            title = it.title,
            subtitle = it.subtitle,
            description = it.description,
            image = it.image,
            numberThread = it.numberThread,
            numberPost = it.numberPost,
            lastUpdatedOn = it.lastUpdatedOn
        )
    }
}

