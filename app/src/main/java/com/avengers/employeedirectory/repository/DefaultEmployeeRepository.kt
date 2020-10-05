package com.avengers.employeedirectory.repository

import android.content.Context
import com.avengers.employeedirectory.Database
import com.avengers.employeedirectory.db.CacheMapper
import com.avengers.employeedirectory.models.Employee
import com.avengers.employeedirectory.network.EmployeeService
import com.avengers.employeedirectory.network.NetworkMapper
import com.avengers.employeedirectory.util.DataState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import java.util.*

interface EmployeeRepository {
    fun getEmployees(forced: Boolean = false): Flow<DataState<List<Employee>>>
    fun getEmployeesSortedByLastName(): Flow<DataState<List<Employee>>>
    fun getEmployeesSortedByFirstName(): Flow<DataState<List<Employee>>>
    fun getEmployeesSortedByTeam(): Flow<DataState<List<Employee>>>
    fun filterByAny(searchTerm: String): Flow<DataState<List<Employee>>>
    fun getEmployee(id: String): Flow<DataState<Employee>>
}

class DefaultEmployeeRepository constructor(@ApplicationContext private val appContext: Context,
                                            private val employeesDataBase: Database,
                                            private val employeeService: EmployeeService,
                                            private val cacheMapper: CacheMapper,
                                            private val networkMapper: NetworkMapper): EmployeeRepository {
    companion object {
        private const val LAST_NETWORK_LOOKUP_EMPLOYEES = "LAST_NETWORK_LOOKUP_EMPLOYEES"
        private const val EMPLOYEES_SHARED_PREFS = "EMPLOYEES_SHARED_PREFS"
        // After you get employee data, don't look again for 6 hours
        private const val EMPLOYEES_CACHE_LIFESPAN = 1000 * 60 * 60 * 12
    }
    override fun getEmployees(forced: Boolean): Flow<DataState<List<Employee>>> =
        flow {
            emit(DataState.Loading)
            try {
                val sharedPrefs = appContext.getSharedPreferences(EMPLOYEES_SHARED_PREFS, Context.MODE_PRIVATE)
                val now = Calendar.getInstance().timeInMillis
                val lastTimeChecked = sharedPrefs.getLong(LAST_NETWORK_LOOKUP_EMPLOYEES, -1)
                if (forced || now > lastTimeChecked + EMPLOYEES_CACHE_LIFESPAN) {
                    val employeeNetworkEntities = employeeService.getEmployees().employees
                    val employees = networkMapper.mapFromEntities(employeeNetworkEntities)
                    val employeeCacheEntities = cacheMapper.mapToEntities(employees)
                    employeeCacheEntities.forEach {
                        employeesDataBase.employeesEntityQueries.insert(it)
                    }
                }
                val cachedEmployees = employeesDataBase.employeesEntityQueries.getEmployeesSortedByLastName()
                    .executeAsList()
                if (cachedEmployees.isNullOrEmpty()) {
                    emit(DataState.Empty)
                } else {
                    emit(DataState.Success(cacheMapper.mapFromEntities(cachedEmployees)))
                }
                sharedPrefs.edit().putLong(LAST_NETWORK_LOOKUP_EMPLOYEES, now).apply()
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }

    override fun getEmployeesSortedByLastName(): Flow<DataState<List<Employee>>> =
        flow {
            emit(DataState.Loading)
            try {
                val cachedEmployees = employeesDataBase
                    .employeesEntityQueries
                    .getEmployeesSortedByLastName()
                    .executeAsList()
                if (cachedEmployees.isNullOrEmpty()) {
                    emit(DataState.Empty)
                } else {
                    emit(DataState.Success(cacheMapper.mapFromEntities(cachedEmployees)))
                }
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }

    override fun getEmployeesSortedByFirstName(): Flow<DataState<List<Employee>>> =
        flow {
            emit(DataState.Loading)
            try {
                val cachedEmployees = employeesDataBase
                    .employeesEntityQueries
                    .getEmployeesSortedByFirstName()
                    .executeAsList()
                if (cachedEmployees.isNullOrEmpty()) {
                    emit(DataState.Empty)
                } else {
                    emit(DataState.Success(cacheMapper.mapFromEntities(cachedEmployees)))
                }
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }

    override fun getEmployeesSortedByTeam(): Flow<DataState<List<Employee>>> =
        flow {
            emit(DataState.Loading)
            try {
                val cachedEmployees = employeesDataBase
                    .employeesEntityQueries
                    .getEmployeesSortedByTeam()
                    .executeAsList()
                if (cachedEmployees.isNullOrEmpty()) {
                    emit(DataState.Empty)
                } else {
                    emit(DataState.Success(cacheMapper.mapFromEntities(cachedEmployees)))
                }
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }

    override fun filterByAny(searchTerm: String): Flow<DataState<List<Employee>>> =
        flow {
            emit(DataState.Searching)
            try {
                val cachedEmployees = employeesDataBase
                    .employeesEntityQueries
                    .filterEmployeesByAny(searchTerm)
                    .executeAsList()
                if (cachedEmployees.isNullOrEmpty()) {
                    emit(DataState.Searching)
                } else {
                    emit(DataState.Success(cacheMapper.mapFromEntities(cachedEmployees)))
                }
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }

    override fun getEmployee(id: String): Flow<DataState<Employee>> =
        flow {
            emit(DataState.Loading)
            try {
                val cachedEmployee = employeesDataBase
                    .employeesEntityQueries
                    .getEmployeeByUUID(id)
                    .executeAsOne()
                emit(DataState.Success(cacheMapper.mapFromEntity(cachedEmployee)))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }
}