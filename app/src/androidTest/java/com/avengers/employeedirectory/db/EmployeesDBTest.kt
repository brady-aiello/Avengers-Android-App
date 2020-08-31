package com.avengers.employeedirectory.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.avengers.CoroutineTestRule
import com.avengers.filterByAny
import com.avengers.testEmployees
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class EmployeesDBTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    lateinit var employeeDataBase: EmployeeDatabase
    lateinit var employeesDao: EmployeesDao
    @Inject
    lateinit var cacheMapper: CacheMapper
    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()
    @ExperimentalCoroutinesApi
    private val testScope = TestCoroutineScope(testDispatcher)

    @ExperimentalCoroutinesApi
    @Before
    fun start() {
        hiltRule.inject()
        employeeDataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            EmployeeDatabase::class.java)
            .setTransactionExecutor(testDispatcher.asExecutor())
            .setQueryExecutor(testDispatcher.asExecutor())
            .build()
        employeesDao = employeeDataBase.employeesDao()

    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetAllSortedByLastName() = testScope.runBlockingTest  {
        val cacheEmployeesEntity = cacheMapper.mapToEntities(testEmployees).sortedBy {
            item -> item.lastName
        }
        employeesDao.insertAll(cacheEmployeesEntity)
        val gotEmployees = employeesDao.getEmployeesSortedByLastName()
        assertEquals(cacheEmployeesEntity, gotEmployees)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetAllSortedByTeam() = testScope.runBlockingTest  {
        employeesDao.insertAll(cacheMapper.mapToEntities(testEmployees))

        val cacheEmployeesEntity = cacheMapper.mapToEntities(testEmployees).sortedBy {
                item -> item.team
        }
        val gotEmployees = employeesDao.getEmployeesSortedByTeam()
        assertEquals(cacheEmployeesEntity, gotEmployees)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetAllSortedByFirstName() = testScope.runBlockingTest  {
        employeesDao.insertAll(cacheMapper.mapToEntities(testEmployees))
        val cacheEmployeesEntity = cacheMapper.mapToEntities(testEmployees).sortedBy {
                item -> item.firstName
        }
        val gotEmployees = employeesDao.getEmployeesSortedByFirstName()
        assertEquals(cacheEmployeesEntity, gotEmployees)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetAllBySearchTerm() = testScope.runBlockingTest  {
        employeesDao.insertAll(cacheMapper.mapToEntities(testEmployees))
        val searchTerm = "iron"
        val cacheEmployeesEntity = cacheMapper.mapToEntities(testEmployees)
            .filterByAny(searchTerm)
        val gotEmployees = employeesDao.filterEmployeesByAny(searchTerm)
        assertEquals(cacheEmployeesEntity, gotEmployees)
    }

    @After
    fun finish() {
        employeeDataBase.close()
    }
}