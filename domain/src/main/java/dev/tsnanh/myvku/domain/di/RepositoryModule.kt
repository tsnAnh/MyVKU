package dev.tsnanh.myvku.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.tsnanh.myvku.domain.repositories.AbsenceRepo
import dev.tsnanh.myvku.domain.repositories.AbsenceRepoImpl
import dev.tsnanh.myvku.domain.repositories.MakeupRepo
import dev.tsnanh.myvku.domain.repositories.MakeupRepoImpl
import dev.tsnanh.myvku.domain.repositories.NewsRepo
import dev.tsnanh.myvku.domain.repositories.NewsRepoImpl
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
    abstract fun bindAbsenceRepo(absenceRepoImpl: AbsenceRepoImpl): AbsenceRepo

    @Binds
    @Singleton
    abstract fun bindMakeupRepo(makeupRepoImpl: MakeupRepoImpl): MakeupRepo

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

