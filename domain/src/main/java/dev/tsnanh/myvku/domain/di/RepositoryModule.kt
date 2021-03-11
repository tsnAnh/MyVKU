package dev.tsnanh.myvku.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.tsnanh.myvku.domain.repositories.NewsRepo
import dev.tsnanh.myvku.domain.repositories.NewsRepoImpl
import dev.tsnanh.myvku.domain.repositories.NoticeRepo
import dev.tsnanh.myvku.domain.repositories.NoticeRepoImpl
import dev.tsnanh.myvku.domain.repositories.NotificationRepo
import dev.tsnanh.myvku.domain.repositories.NotificationRepoImpl
import dev.tsnanh.myvku.domain.repositories.TeacherRepo
import dev.tsnanh.myvku.domain.repositories.TeacherRepoImpl
import dev.tsnanh.myvku.domain.repositories.TimetableRepo
import dev.tsnanh.myvku.domain.repositories.TimetableRepoImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindNewsRepo(newsRepoImpl: NewsRepoImpl): NewsRepo

    @Binds
    @Singleton
    abstract fun bindNoticeRepo(noticeRepoImpl: NoticeRepoImpl): NoticeRepo

    @Binds
    @Singleton
    abstract fun bindNotificationRepo(notificationRepoImpl: NotificationRepoImpl): NotificationRepo

    @Binds
    @Singleton
    abstract fun bindTeacherRepo(teacherRepoImpl: TeacherRepoImpl): TeacherRepo

    @Binds
    @Singleton
    abstract fun bindTimetableRepo(timetableRepoImpl: TimetableRepoImpl): TimetableRepo
}

