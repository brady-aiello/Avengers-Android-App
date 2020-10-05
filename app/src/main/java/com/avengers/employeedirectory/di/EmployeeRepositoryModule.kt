package com.avengers.employeedirectory.di

import android.content.Context
import com.avengers.employeedirectory.Database
import com.avengers.employeedirectory.db.CacheMapper
import com.avengers.employeedirectory.network.EmployeeService
import com.avengers.employeedirectory.network.NetworkMapper
import com.avengers.employeedirectory.repository.DefaultEmployeeRepository
import com.avengers.employeedirectory.repository.EmployeeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object EmployeeRepositoryModule {

    @Singleton
    @Provides
    fun provideEmployeeRepository(
        @ApplicationContext context: Context,
        employeesDatabase: Database,
        employeeService: EmployeeService,
        cacheMapper: CacheMapper,
        networkMapper: NetworkMapper
    ): EmployeeRepository =
        DefaultEmployeeRepository(context, employeesDatabase, employeeService, cacheMapper, networkMapper)
}