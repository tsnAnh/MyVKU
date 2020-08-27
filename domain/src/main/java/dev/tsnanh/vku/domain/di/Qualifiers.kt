package dev.tsnanh.vku.domain.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CreateNewThreadUseCase

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CreateNewReplyUseCase

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UpdateReplyUseCase