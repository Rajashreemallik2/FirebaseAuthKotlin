package com.example.firebaseauthkotlin.dependency

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {

    @Provides
    @ViewModelScoped
    fun provideApplicationContext(
        @ApplicationContext context: Context
    ) = context

    @Provides
    @ViewModelScoped
    fun provideMainDispatcher() = Dispatchers.Main as CoroutineDispatcher
}