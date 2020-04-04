package dev.tsnanh.vku.database

import androidx.room.*
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import dev.tsnanh.vku.domain.Forum
import dev.tsnanh.vku.domain.News
import dev.tsnanh.vku.domain.Post
import dev.tsnanh.vku.domain.User

@Entity(tableName = "news")
data class DatabaseNews(
    @PrimaryKey
    var url: String,
    var title: String,
    var category: String,
    var date: String
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