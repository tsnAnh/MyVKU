package dev.tsnanh.vku.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.tsnanh.vku.domain.repositories.*
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindForumRepo(forumRepoImpl: ForumRepoImpl): ForumRepo

    @Singleton
    @Binds
    abstract fun bindNewsRepo(newsRepoImpl: NewsRepoImpl): NewsRepo

    @Singleton
    @Binds
    abstract fun bindNoticeRepo(noticeRepoImpl: NoticeRepoImpl): NoticeRepo

    @Singleton
    @Binds
    abstract fun bindNotificationRepo(notificationRepoImpl: NotificationRepoImpl): NotificationRepo

    @Singleton
    @Binds
    abstract fun bindReplyRepo(replyRepoImpl: ReplyRepoImpl): ReplyRepo

    @Singleton
    @Binds
    abstract fun bindTeacherRepo(teacherRepoImpl: TeacherRepoImpl): TeacherRepo

    @Singleton
    @Binds
    abstract fun bindThreadRepo(threadRepoImpl: ThreadRepoImpl): ThreadRepo

    @Singleton
    @Binds
    abstract fun bindTimetableRepo(timetableRepoImpl: TimetableRepoImpl): TimetableRepo

    @Singleton
    @Binds
    abstract fun bindUserRepo(userRepoImpl: UserRepoImpl): UserRepo
}

