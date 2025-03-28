package com.example.studyflow.di

import com.example.studyflow.data.auth.FirebaseAuthRepositoryImpl
import com.example.studyflow.domain.auth.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideAuthRepository(): AuthRepository = FirebaseAuthRepositoryImpl()
}