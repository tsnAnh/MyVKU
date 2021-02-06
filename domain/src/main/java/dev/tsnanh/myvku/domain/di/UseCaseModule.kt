package dev.tsnanh.myvku.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.tsnanh.myvku.domain.usecases.*
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class UseCaseModule {
    @Singleton
    @Binds
    abstract fun bindRetrieveNewsUseCase(retrieveNewsUseCaseImpl: RetrieveNewsUseCaseImpl): RetrieveNewsUseCase

    @Singleton
    @Binds
    abstract fun bindRetrieveNoticeUseCase(retrieveNoticeUseCaseImpl: RetrieveNoticeUseCaseImpl): RetrieveNoticeUseCase

    @Singleton
    @Binds
    abstract fun bindRetrieveNotificationUseCase(retrieveNotificationsUseCaseImpl: RetrieveNotificationsUseCaseImpl): RetrieveNotificationsUseCase

    @Singleton
    @Binds
    abstract fun bindRetrieveTeacherUseCase(retrieveTeachersUseCaseImpl: RetrieveTeachersUseCaseImpl): RetrieveTeachersUseCase

    @Singleton
    @Binds
    abstract fun bindRetrieveUserTimetableUseCase(retrieveUserTimetableUseCaseImpl: RetrieveUserTimetableUseCaseImpl): RetrieveUserTimetableUseCase

}