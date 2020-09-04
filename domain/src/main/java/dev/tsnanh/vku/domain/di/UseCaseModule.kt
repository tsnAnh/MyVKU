package dev.tsnanh.vku.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.tsnanh.vku.domain.usecases.*
import dev.tsnanh.vku.domain.usecases.CreateNewReplyUseCase
import dev.tsnanh.vku.domain.usecases.CreateNewThreadUseCase
import dev.tsnanh.vku.domain.usecases.UpdateReplyUseCase
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class UseCaseModule {
    @dev.tsnanh.vku.domain.di.CreateNewReplyUseCase
    @Singleton
    @Binds
    abstract fun bindCreateNewReplyUseCase(createNewReplyUseCaseImpl: CreateNewReplyUseCaseImpl): CreateNewReplyUseCase

    @dev.tsnanh.vku.domain.di.CreateNewThreadUseCase
    @Singleton
    @Binds
    abstract fun bindCreateNewThreadUseCase(createNewThreadUseCaseImpl: CreateNewThreadUseCaseImpl): CreateNewThreadUseCase

    @Singleton
    @Binds
    abstract fun bindDeleteThreadUseCase(deleteThreadUseCaseImpl: DeleteThreadUseCaseImpl): DeleteThreadUseCase

    @Singleton
    @Binds
    abstract fun bindLoginUseCase(loginUseCaseImpl: LoginUseCaseImpl): LoginUseCase

    @Singleton
    @Binds
    abstract fun bindRetrieveForumUseCase(retrieveForumsUseCaseImpl: RetrieveForumsUseCaseImpl): RetrieveForumsUseCase

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
    abstract fun bindRetrievePageCountOfThreadUseCase(retrievePageCountOfThreadUseCaseImpl: RetrievePageCountOfThreadUseCaseImpl): RetrievePageCountOfThreadUseCase

    @Singleton
    @Binds
    abstract fun bindRetrieveRepliesUseCase(retrieveRepliesUseCaseImpl: RetrieveRepliesUseCaseImpl): RetrieveRepliesUseCase

    @Singleton
    @Binds
    abstract fun bindRetrieveReplyByIdUseCase(retrieveReplyByIdUseCaseImpl: RetrieveReplyByIdUseCaseImpl): RetrieveReplyByIdUseCase

    @Singleton
    @Binds
    abstract fun bindRetrieveTeacherUseCase(retrieveTeachersUseCaseImpl: RetrieveTeachersUseCaseImpl): RetrieveTeachersUseCase

    @Singleton
    @Binds
    abstract fun bindRetrieveThreadsUseCase(retrieveThreadsUseCaseImpl: RetrieveThreadsUseCaseImpl): RetrieveThreadsUseCase

    @Singleton
    @Binds
    abstract fun bindRetrieveUserTimetableLiveDataUseCase(retrieveUserTimetableLiveDataUseCaseImpl: RetrieveUserTimetableLiveDataUseCaseImpl): RetrieveUserTimetableLiveDataUseCase

    @Singleton
    @Binds
    abstract fun bindRetrieveUserTimetableUseCase(retrieveUserTimetableUseCaseImpl: RetrieveUserTimetableUseCaseImpl): RetrieveUserTimetableUseCase

    @dev.tsnanh.vku.domain.di.UpdateReplyUseCase
    @Singleton
    @Binds
    abstract fun bindUpdateReplyUseCase(updateReplyUseCaseImpl: UpdateReplyUseCaseImpl): UpdateReplyUseCase

    @Singleton
    @Binds
    abstract fun bindUpdateThreadTitleUseCase(updateThreadTitleUseCaseImpl: UpdateThreadTitleUseCaseImpl): UpdateThreadTitleUseCase
}