/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.tsnanh.vku.domain.News

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