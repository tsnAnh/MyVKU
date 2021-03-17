package dev.tsnanh.myvku.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.tsnanh.myvku.domain.usecases.RetrieveAbsencesUseCase
import dev.tsnanh.myvku.domain.usecases.RetrieveAbsencesUseCaseImpl
import dev.tsnanh.myvku.domain.usecases.RetrieveMakeupClassesUseCase
import dev.tsnanh.myvku.domain.usecases.RetrieveMakeupClassesUseCaseImpl
import dev.tsnanh.myvku.domain.usecases.RetrieveNewsUseCase
import dev.tsnanh.myvku.domain.usecases.RetrieveNewsUseCaseImpl
import dev.tsnanh.myvku.domain.usecases.RetrieveNotificationsUseCase
import dev.tsnanh.myvku.domain.usecases.RetrieveNotificationsUseCaseImpl
import dev.tsnanh.myvku.domain.usecases.RetrieveTeachersUseCase
import dev.tsnanh.myvku.domain.usecases.RetrieveTeachersUseCaseImpl
import dev.tsnanh.myvku.domain.usecases.RetrieveTimetableUseCase
import dev.tsnanh.myvku.domain.usecases.RetrieveTimetableUseCaseImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    @Singleton
    abstract fun bindRetrieveNewsUseCase(retrieveNewsUseCaseImpl: RetrieveNewsUseCaseImpl): RetrieveNewsUseCase

    @Binds
    @Singleton
    abstract fun bindRetrieveNotificationUseCase(retrieveNotificationsUseCaseImpl: RetrieveNotificationsUseCaseImpl): RetrieveNotificationsUseCase

    @Binds
    @Singleton
    abstract fun bindRetrieveTeacherUseCase(retrieveTeachersUseCaseImpl: RetrieveTeachersUseCaseImpl): RetrieveTeachersUseCase

    @Binds
    @Singleton
    abstract fun bindRetrieveUserTimetableUseCase(retrieveUserTimetableUseCaseImpl: RetrieveTimetableUseCaseImpl): RetrieveTimetableUseCase

    @Binds
    @Singleton
    abstract fun bindRetrieveAbsencesUseCase(retrieveAbsenceUseCaseImpl: RetrieveAbsencesUseCaseImpl): RetrieveAbsencesUseCase

    @Binds
    abstract fun bindRetrieveMakeupClassesUseCase(retrieveMakeupClassesUseCaseImpl: RetrieveMakeupClassesUseCaseImpl): RetrieveMakeupClassesUseCase
}