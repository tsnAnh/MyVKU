package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.CreateThreadContainer
import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.entities.ThreadContainer
import dev.tsnanh.vku.domain.network.VKUServiceApi

interface ThreadRepo {
    suspend fun getThreads(forumId: String): ThreadContainer
    suspend fun createThread(
        idToken: String,
        container: CreateThreadContainer,
        forumId: String
    ): ForumThread
}

class ThreadRepoImpl : ThreadRepo {
    override suspend fun getThreads(forumId: String) = VKUServiceApi.network.getThreads(forumId)
    override suspend fun createThread(
        idToken: String,
        container: CreateThreadContainer,
        forumId: String
    ) =
        VKUServiceApi.network.createThread(idToken, container, forumId)
}