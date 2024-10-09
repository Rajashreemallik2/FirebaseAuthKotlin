package com.example.firebaseauthkotlin.dependencies

import com.example.firebaseauthkotlin.repositories.AuthRepository
import com.example.firebaseauthkotlin.repositories.FirebaseAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {

    @Provides
    @ViewModelScoped
    fun provideAuthRepository() = FirebaseAuthRepository() as AuthRepository
}