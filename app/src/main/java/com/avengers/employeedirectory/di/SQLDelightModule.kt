package com.avengers.employeedirectory.di

import android.content.Context
import com.avengers.employeedirectory.Database
import com.avengers.employeedirectory.sqldelight.EmployeeCacheEntity
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object SQLDelightModule {

    @Singleton
    @Provides
    fun provideAndroidDriver(@ApplicationContext applicationContext: Context): AndroidSqliteDriver {
        return AndroidSqliteDriver(
            schema = Database.Schema,
            context = applicationContext,
            name = "employees.db"
        )
    }

    @Singleton
    @Provides
    fun provideSQLDelightDatabase(driver: AndroidSqliteDriver): Database {
        return Database(driver)
    }
}