package dev.tsnanh.myvku.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.tsnanh.myvku.domain.usecases.RetrieveNewsUseCase
import dev.tsnanh.myvku.domain.usecases.RetrieveNewsUseCaseImpl
import dev.tsnanh.myvku.domain.usecases.RetrieveNoticeUseCase
import dev.tsnanh.myvku.domain.usecases.RetrieveNoticeUseCaseImpl
import dev.tsnanh.myvku.domain.usecases.RetrieveNotificationsUseCase
import dev.tsnanh.myvku.domain.usecases.RetrieveNotificationsUseCaseImpl
import dev.tsnanh.myvku.domain.usecases.RetrieveTeachersUseCase
import dev.tsnanh.myvku.domain.usecases.RetrieveTeachersUseCaseImpl
import dev.tsnanh.myvku.domain.usecases.RetrieveUserTimetableUseCase
import dev.tsnanh.myvku.domain.usecases.RetrieveUserTimetableUseCaseImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    @Singleton
    abstract fun bindRetrieveNewsUseCase(retrieveNewsUseCaseImpl: RetrieveNewsUseCaseImpl): RetrieveNewsUseCase

    @Binds
    @Singleton
    abstract fun bindRetrieveNoticeUseCase(retrieveNoticeUseCaseImpl: RetrieveNoticeUseCaseImpl): RetrieveNoticeUseCase

    @Binds
    @Singleton
    abstract fun bindRetrieveNotificationUseCase(retrieveNotificationsUseCaseImpl: RetrieveNotificationsUseCaseImpl): RetrieveNotificationsUseCase

    @Binds
    @Singleton
    abstract fun bindRetrieveTeacherUseCase(retrieveTeachersUseCaseImpl: RetrieveTeachersUseCaseImpl): RetrieveTeachersUseCase

    @Binds
    @Singleton
    abstract fun bindRetrieveUserTimetableUseCase(retrieveUserTimetableUseCaseImpl: RetrieveUserTimetableUseCaseImpl): RetrieveUserTimetableUseCase

}