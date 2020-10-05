package com.avengers.employeedirectory

import com.avengers.CoroutineTestRule
import com.avengers.employeedirectory.db.CacheMapper
import com.avengers.employeedirectory.sqldelight.EmployeesEntityQueries
import com.avengers.filterByAny
import com.avengers.testEmployees
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class EmployeesDBTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    lateinit var sqliteDriver: JdbcSqliteDriver
    lateinit var queries: EmployeesEntityQueries

    private val cacheMapper: CacheMapper = CacheMapper()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()
    @ExperimentalCoroutinesApi
    private val testScope = TestCoroutineScope(testDispatcher)


    @ExperimentalCoroutinesApi
    @Before
    fun start() {
        sqliteDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        Database.Schema.create(sqliteDriver)
        queries = Database(sqliteDriver).employeesEntityQueries
        cacheMapper.mapToEntities(testEmployees).forEach { queries.insert(it) }
    }


    @ExperimentalCoroutinesApi
    @Test
    fun testGetAllSortedByLastName() = testScope.runBlockingTest  {
        val cacheEmployeesEntity =
                cacheMapper.mapToEntities(testEmployees).sortedBy { item -> item.lastName }
        val gotEmployees =
                queries.getEmployeesSortedByLastName().executeAsList()
        assertEquals(cacheEmployeesEntity, gotEmployees)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun testGetAllSortedByTeam() = testScope.runBlockingTest  {
        val cacheEmployeesEntity =
                cacheMapper.mapToEntities(testEmployees).sortedBy { item -> item.team }
        val gotEmployees = queries.getEmployeesSortedByTeam().executeAsList()
        assertEquals(cacheEmployeesEntity, gotEmployees)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun testGetAllSortedByFirstName() = testScope.runBlockingTest  {
        val cacheEmployeesEntity =
                cacheMapper.mapToEntities(testEmployees).sortedBy { item -> item.firstName }
        val gotEmployees =
                queries.getEmployeesSortedByFirstName().executeAsList()
        assertEquals(cacheEmployeesEntity, gotEmployees)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun testGetAllBySearchTerm() = testScope.runBlockingTest  {
        val searchTerm = "iron"
        val cacheEmployeesEntity = cacheMapper.mapToEntities(testEmployees)
            .filterByAny(searchTerm)
        val gotEmployees =
                queries.filterEmployeesByAny(searchTerm).executeAsList()
        assertEquals(cacheEmployeesEntity, gotEmployees)
    }


    @After
    fun finish() {
        sqliteDriver.close()
    }
}