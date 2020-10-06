package com.avengers.employeedirectory.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.avengers.CoroutineTestRule
import com.avengers.employeedirectory.di.EmployeeRepositoryModule
import com.avengers.employeedirectory.models.Employee
import com.avengers.employeedirectory.repository.EmployeeRepository
import com.avengers.employeedirectory.util.DataState
import com.avengers.employeedirectory.util.EmployeesStateEvent
import com.avengers.filterByAnyTerm
import com.avengers.getOrAwaitValue
import com.avengers.testEmployees
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Singleton

@UninstallModules(EmployeeRepositoryModule::class)
@HiltAndroidTest
class MainViewModelTest {
    lateinit var mainViewModel: MainViewModel

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var employeeRepository: EmployeeRepository

    @ExperimentalCoroutinesApi
    @Before
    fun start() {
        hiltRule.inject()
        mainViewModel = MainViewModel(
            repository = employeeRepository,
            dispatcherProvider = coroutinesTestRule.testDispatcherProvider)
        assertNotNull(mainViewModel)
    }

    @FlowPreview
    @Test
    fun testGetEmployeesSortedByLastNameEvent() {
        mainViewModel
            .setStateEvent(EmployeesStateEvent.GetEmployeesSortedByLastNameEvent)
        assertEquals(
            DataState.Success(testEmployees.sortedBy { it.lastName }),
            mainViewModel.dataState.getOrAwaitValue()
        )
    }

    @FlowPreview
    @Test
    fun testGetEmployeesSortedByFirstNameEvent() {
        mainViewModel
            .setStateEvent(EmployeesStateEvent.GetEmployeesSortedByFirstNameEvent)
        assertEquals(
            DataState.Success(testEmployees.sortedBy { it.firstName }),
            mainViewModel.dataState.getOrAwaitValue()
        )
    }

    @FlowPreview
    @Test
    fun testGetEmployeesSortedByTeamEvent() {
        mainViewModel
            .setStateEvent(EmployeesStateEvent.GetEmployeesSortedByTeamEvent)
        assertEquals(DataState.Success(testEmployees.sortedBy { it.team }),
            mainViewModel.dataState.getOrAwaitValue())
    }

    @FlowPreview
    @Test
    fun testEmployeesByAnyEvent() {
        val searchTerm = "Stark"
        mainViewModel
            .setStateEvent(EmployeesStateEvent.FilterEmployeesByAnyEvent(searchTerm))
        assertEquals(DataState.Success(testEmployees.filter { it.lastName == searchTerm }),
            mainViewModel.dataState.getOrAwaitValue())
    }

    @FlowPreview
    @Test
    fun testGetEmployeeDetailEvent() {
        val employee = testEmployees.filter { it.lastName == "Stark" }[0]

        mainViewModel
            .currentEmployee.value = employee
        assertEquals(employee,
            mainViewModel.currentEmployee.getOrAwaitValue())
    }

    @Module
    @InstallIn(ApplicationComponent::class)
    object RepositoryModule {
        @Singleton
        @Provides
        fun provideFakeEmployeeSuccessRepository(): EmployeeRepository =
            object: EmployeeRepository {
                override fun getEmployees(forced: Boolean): Flow<DataState<List<Employee>>> =
                    flow {
                        emit(DataState.Loading)
                        emit(DataState.Success(testEmployees.sortedBy { it.lastName }))
                    }

                override fun getEmployeesSortedByLastName(): Flow<DataState<List<Employee>>> =
                    getEmployees()

                override fun getEmployeesSortedByFirstName(): Flow<DataState<List<Employee>>> =
                    flow {
                        emit(DataState.Loading)
                        emit(DataState.Success(testEmployees.sortedBy { it.firstName }))
                    }

                override fun getEmployeesSortedByTeam(): Flow<DataState<List<Employee>>> =
                    flow {
                        emit(DataState.Loading)
                        emit(DataState.Success(testEmployees.sortedBy { it.team }))
                    }

                override fun filterByAny(searchTerm: String): Flow<DataState<List<Employee>>> =
                    flow {
                        emit(DataState.Loading)
                        emit(DataState.Success(testEmployees.filterByAnyTerm(searchTerm)
                            .sortedBy { it.lastName } ))
                    }

                override fun getEmployee(id: String): Flow<DataState<Employee>> =
                    flow {
                        emit(DataState.Loading)
                        emit(DataState.Success(testEmployees.filter { it.uuid == id }[0]))
                    }

            }
    }
}