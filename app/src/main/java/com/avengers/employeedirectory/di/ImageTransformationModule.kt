package com.avengers.employeedirectory.di

import android.util.LruCache
import com.commit451.coiltransformations.facedetection.CenterOnFaceTransformation

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object ImageTransformationModule {
    @Singleton
    @Provides
    fun provideImageTransformationLruCache(): LruCache<String, String> {
        return LruCache(4 * 1024 * 1024)
    }

    @Singleton
    @Provides
    fun provideCenterOnFaceTransformation(cache: LruCache<String, String>):
            CenterOnFaceTransformation {
        return CenterOnFaceTransformation(cache, 65)
    }
}