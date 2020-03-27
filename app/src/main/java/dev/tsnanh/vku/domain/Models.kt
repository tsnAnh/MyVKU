package dev.tsnanh.vku.domain

data class News(
    var url: String,
    var title: String,
    var date: String,
    var category: String
)

data class Forum(
    var id: String,
    var title: String,
    var subtitle: String,
    var description: String,
    var image: String,
    var numberThread: Int,
    var numberPost: Int,
    var lastUpdatedOn: String
)