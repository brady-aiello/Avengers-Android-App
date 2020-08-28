package com.avengers.employeedirectory.repository

import android.content.Context
import com.avengers.employeedirectory.db.CacheMapper
import com.avengers.employeedirectory.db.EmployeesDao
import com.avengers.employeedirectory.models.Employee
import com.avengers.employeedirectory.network.EmployeeService
import com.avengers.employeedirectory.network.NetworkMapper
import com.avengers.employeedirectory.util.DataState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import java.util.*

class EmployeeRepository constructor(@ApplicationContext private val appContext: Context,
                                     private val employeesDao: EmployeesDao,
                                     private val employeeService: EmployeeService,
                                     private val cacheMapper: CacheMapper,
                                     private val networkMapper: NetworkMapper) {
    companion object {
        private const val LAST_NETWORK_LOOKUP_EMPLOYEES = "LAST_NETWORK_LOOKUP_EMPLOYEES"
        private const val EMPLOYEES_SHARED_PREFS = "EMPLOYEES_SHARED_PREFS"
        // After you get employee data, don't look again for
        private const val EMPLOYEES_CACHE_LIFESPAN = 1000 * 60 * 60 * 12
    }
    fun getEmployees(forced: Boolean = false): Flow<DataState<List<Employee>>> =
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
                    employeesDao.insertAll(employeeCacheEntities)
                }
                val cachedEmployees = employeesDao.getEmployeesSortedByLastName()
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

    fun getEmployeesSortedByLastName(): Flow<DataState<List<Employee>>> =
        flow {
            emit(DataState.Loading)
            try {
                val cachedEmployees = employeesDao.getEmployeesSortedByLastName()
                if (cachedEmployees.isNullOrEmpty()) {
                    emit(DataState.Empty)
                } else {
                    emit(DataState.Success(cacheMapper.mapFromEntities(cachedEmployees)))
                }
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }

    fun getEmployeesSortedByFirstName(): Flow<DataState<List<Employee>>> =
        flow {
            emit(DataState.Loading)
            try {
                val cachedEmployees = employeesDao.getEmployeesSortedByFirstName()
                if (cachedEmployees.isNullOrEmpty()) {
                    emit(DataState.Empty)
                } else {
                    emit(DataState.Success(cacheMapper.mapFromEntities(cachedEmployees)))
                }
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }

    fun getEmployeesSortedByTeam(): Flow<DataState<List<Employee>>> =
        flow {
            emit(DataState.Loading)
            try {
                val cachedEmployees = employeesDao.getEmployeesSortedByTeam()
                if (cachedEmployees.isNullOrEmpty()) {
                    emit(DataState.Empty)
                } else {
                    emit(DataState.Success(cacheMapper.mapFromEntities(cachedEmployees)))
                }
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }

    fun filterByAny(searchTerm: String): Flow<DataState<List<Employee>>> =
        flow {
            emit(DataState.Searching)
            try {
                val cachedEmployees = employeesDao.filterEmployeesByAny("%$searchTerm%")
                if (cachedEmployees.isNullOrEmpty()) {
                    emit(DataState.Searching)
                } else {
                    emit(DataState.Success(cacheMapper.mapFromEntities(cachedEmployees)))
                }
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }
    fun getEmployee(id: String): Flow<DataState<Employee>> =
        flow {
            emit(DataState.Loading)
            try {
                val cachedEmployee = employeesDao.getEmployee(id)
                emit(DataState.Success(cacheMapper.mapFromEntity(cachedEmployee)))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }
}