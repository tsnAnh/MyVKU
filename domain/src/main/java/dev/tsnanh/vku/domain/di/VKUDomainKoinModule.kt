package dev.tsnanh.vku.domain.di

import androidx.room.Room
import dev.tsnanh.vku.domain.database.VKUDao
import dev.tsnanh.vku.domain.database.VKUDatabase
import dev.tsnanh.vku.domain.repositories.ForumRepo
import dev.tsnanh.vku.domain.repositories.ForumRepoImpl
import dev.tsnanh.vku.domain.repositories.NewsRepo
import dev.tsnanh.vku.domain.repositories.NewsRepoImpl
import dev.tsnanh.vku.domain.repositories.NotificationRepo
import dev.tsnanh.vku.domain.repositories.NotificationRepoImpl
import dev.tsnanh.vku.domain.repositories.ReplyRepo
import dev.tsnanh.vku.domain.repositories.ReplyRepoImpl
import dev.tsnanh.vku.domain.repositories.TeacherRepo
import dev.tsnanh.vku.domain.repositories.TeacherRepoImpl
import dev.tsnanh.vku.domain.repositories.ThreadRepo
import dev.tsnanh.vku.domain.repositories.ThreadRepoImpl
import dev.tsnanh.vku.domain.repositories.TimetableRepo
import dev.tsnanh.vku.domain.repositories.TimetableRepoImpl
import dev.tsnanh.vku.domain.repositories.UserRepo
import dev.tsnanh.vku.domain.repositories.UserRepoImpl
import dev.tsnanh.vku.domain.usecases.CreateNewReplyUseCase
import dev.tsnanh.vku.domain.usecases.CreateNewReplyUseCaseImpl
import dev.tsnanh.vku.domain.usecases.CreateNewThreadUseCase
import dev.tsnanh.vku.domain.usecases.CreateNewThreadUseCaseImpl
import dev.tsnanh.vku.domain.usecases.LoginUseCase
import dev.tsnanh.vku.domain.usecases.LoginUseCaseImpl
import dev.tsnanh.vku.domain.usecases.RetrieveForumsUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveForumsUseCaseImpl
import dev.tsnanh.vku.domain.usecases.RetrieveNewsUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveNewsUseCaseImpl
import dev.tsnanh.vku.domain.usecases.RetrieveNotificationsUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveNotificationsUseCaseImpl
import dev.tsnanh.vku.domain.usecases.RetrievePageCountOfThreadUseCase
import dev.tsnanh.vku.domain.usecases.RetrievePageCountOfThreadUseCaseImpl
import dev.tsnanh.vku.domain.usecases.RetrieveRepliesUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveRepliesUseCaseImpl
import dev.tsnanh.vku.domain.usecases.RetrieveReplyByIdUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveReplyByIdUseCaseImpl
import dev.tsnanh.vku.domain.usecases.RetrieveSingleForumUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveSingleForumUseCaseImpl
import dev.tsnanh.vku.domain.usecases.RetrieveTeachersUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveTeachersUseCaseImpl
import dev.tsnanh.vku.domain.usecases.RetrieveThreadsUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveThreadsUseCaseImpl
import dev.tsnanh.vku.domain.usecases.RetrieveUserTimetableLiveDataUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveUserTimetableLiveDataUseCaseImpl
import dev.tsnanh.vku.domain.usecases.RetrieveUserTimetableUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveUserTimetableUseCaseImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val module = module {
    single<VKUDatabase> {
        synchronized(androidContext()) {
            Room
                .databaseBuilder(
                    androidContext(),
                    VKUDatabase::class.java,
                    "vku_database"
                )
                .build()
        }
    }

    // DAO
    single<VKUDao> {
        get<VKUDatabase>().dao
    }
    single<NewsRepo> {
        NewsRepoImpl()
    }
    single<ForumRepo> {
        ForumRepoImpl()
    }
    single<UserRepo> {
        UserRepoImpl()
    }
    single<ReplyRepo> {
        ReplyRepoImpl()
    }
    single<RetrieveNewsUseCase> {
        RetrieveNewsUseCaseImpl()
    }
    single<RetrieveForumsUseCase> {
        RetrieveForumsUseCaseImpl()
    }
    single<RetrieveThreadsUseCase> {
        RetrieveThreadsUseCaseImpl()
    }
    single<RetrieveRepliesUseCase> {
        RetrieveRepliesUseCaseImpl()
    }
    single<RetrieveReplyByIdUseCase> {
        RetrieveReplyByIdUseCaseImpl()
    }
    single<RetrievePageCountOfThreadUseCase> {
        RetrievePageCountOfThreadUseCaseImpl()
    }
    single<LoginUseCase> {
        LoginUseCaseImpl()
    }
    single<TimetableRepo> {
        TimetableRepoImpl()
    }
    single<ThreadRepo> {
        ThreadRepoImpl()
    }
    single<RetrieveUserTimetableLiveDataUseCase> {
        RetrieveUserTimetableLiveDataUseCaseImpl()
    }
    single<RetrieveUserTimetableUseCase> {
        RetrieveUserTimetableUseCaseImpl()
    }
    single<RetrieveSingleForumUseCase> {
        RetrieveSingleForumUseCaseImpl()
    }
    single<CreateNewThreadUseCase> {
        CreateNewThreadUseCaseImpl()
    }
    single<CreateNewReplyUseCase> {
        CreateNewReplyUseCaseImpl()
    }
    single<NotificationRepo> {
        NotificationRepoImpl()
    }
    single<RetrieveNotificationsUseCase> {
        RetrieveNotificationsUseCaseImpl()
    }
    single<RetrieveTeachersUseCase> {
        RetrieveTeachersUseCaseImpl()
    }
    single<TeacherRepo> {
        TeacherRepoImpl()
    }
}