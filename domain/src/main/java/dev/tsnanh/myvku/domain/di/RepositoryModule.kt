package dev.tsnanh.myvku.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.tsnanh.myvku.domain.repositories.*
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {
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
    abstract fun bindTeacherRepo(teacherRepoImpl: TeacherRepoImpl): TeacherRepo

    @Singleton
    @Binds
    abstract fun bindTimetableRepo(timetableRepoImpl: TimetableRepoImpl): TimetableRepo
}

