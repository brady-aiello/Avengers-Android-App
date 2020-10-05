package com.avengers.employeedirectory.di

import android.content.Context
import android.transition.TransitionInflater
import coil.ImageLoader
import coil.request.CachePolicy
import coil.util.CoilUtils
import com.avengers.employeedirectory.R
import com.avengers.employeedirectory.network.EmployeeService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule {

    /**
     * Adding a custom HttpLoggingInterceptor so we can see our HTTP requests when debugging.
     */
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    @Singleton
    @Provides
    fun provideHttpClient(okHttpClientBuilder: OkHttpClient.Builder,
                          httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return okHttpClientBuilder.apply {
            addInterceptor(httpLoggingInterceptor)
        }.build()
    }


    @ExperimentalSerializationApi
    @Singleton
    @Provides
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://bba-avengers.s3.us-west-2.amazonaws.com/")
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
    }

    @Singleton
    @Provides
    fun provideEmployeeService(retrofitBuilder: Retrofit.Builder): EmployeeService {
        return retrofitBuilder
            .build()
            .create(EmployeeService::class.java)
    }

    @Singleton
    @Provides
    // Give coil a separate cache from the injected OkHttpClient
    fun provideCoilImageLoader(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .allowHardware(false)
            .crossfade(false)
//            .diskCachePolicy(CachePolicy.DISABLED)
//            .memoryCachePolicy(CachePolicy.DISABLED)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(context))
                    .build()
            }
            .build()
    }
}