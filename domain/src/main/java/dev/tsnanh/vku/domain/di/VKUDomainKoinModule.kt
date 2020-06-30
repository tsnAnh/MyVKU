package dev.tsnanh.vku.domain.di

import androidx.room.Room
import dev.tsnanh.vku.domain.database.VKUDao
import dev.tsnanh.vku.domain.database.VKUDatabase
import dev.tsnanh.vku.domain.repositories.*
import dev.tsnanh.vku.domain.usecases.*
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
}