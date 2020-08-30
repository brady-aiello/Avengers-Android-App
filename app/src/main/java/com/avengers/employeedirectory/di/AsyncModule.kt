package com.avengers.employeedirectory.di

import com.avengers.employeedirectory.async.DefaultDispatcherProvider
import com.avengers.employeedirectory.async.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AsyncModule {
    @Singleton
    @Provides
    fun provideCoroutineDispatcherProvider(): DispatcherProvider
            = DefaultDispatcherProvider()
}