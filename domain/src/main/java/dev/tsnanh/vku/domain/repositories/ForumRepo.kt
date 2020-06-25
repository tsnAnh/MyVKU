package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.NetworkCustomForum
import dev.tsnanh.vku.domain.entities.ReplyContainer
import dev.tsnanh.vku.domain.network.VKUServiceApi

interface ForumRepo {
    suspend fun getForums(): List<NetworkCustomForum>
    suspend fun getReplies(threadId: String, page: Int, limit: Int): ReplyContainer
}

class ForumRepoImpl : ForumRepo {
    override suspend fun getForums() = VKUServiceApi.network.getForums()

    override suspend fun getReplies(threadId: String, page: Int, limit: Int) =
        VKUServiceApi.network.getRepliesInThread(threadId, page, limit)
}