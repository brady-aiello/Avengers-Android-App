package com.avengers.employeedirectory.di

import android.content.Context
import androidx.room.Room
import com.avengers.employeedirectory.db.EmployeeDatabase
import com.avengers.employeedirectory.db.EmployeesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RoomModule {
    @Singleton
    @Provides
    fun provideEmployeeDatabase(@ApplicationContext context: Context): EmployeeDatabase {
        return Room.databaseBuilder(context,
            EmployeeDatabase::class.java,
            EmployeeDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideEmployeesDao(employeeDatabase: EmployeeDatabase): EmployeesDao =
        employeeDatabase.employeesDao()
}