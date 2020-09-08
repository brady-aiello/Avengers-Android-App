package com.avengers.employeedirectory.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.employeedirectory.async.DispatcherProvider
import com.avengers.employeedirectory.models.Employee
import com.avengers.employeedirectory.repository.EmployeeRepository
import com.avengers.employeedirectory.util.DataState
import com.avengers.employeedirectory.util.EmployeesStateEvent
import com.avengers.employeedirectory.util.EmployeesStateEvent.*
import com.avengers.employeedirectory.util.Event
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel @ViewModelInject constructor(private val repository: EmployeeRepository,
                                                 private val dispatcherProvider: DispatcherProvider):
    ViewModel() {

    private val _dataState: MutableLiveData<DataState<List<Employee>>> = MutableLiveData()
    val dataState: LiveData<DataState<List<Employee>>>
        get() = _dataState
    private val _oneTimeNavigateEvent: MutableLiveData<Event<GetEmployeeDetailEvent>> = MutableLiveData()

    val oneTimeNavigateEvent: LiveData<Event<GetEmployeeDetailEvent>> = _oneTimeNavigateEvent
    private val _currentEmployee: MutableLiveData<Employee> = MutableLiveData()
    val currentEmployee: LiveData<Employee>
        get() = _currentEmployee

    @FlowPreview
    fun setStateEvent(stateEvent: EmployeesStateEvent) {
        viewModelScope.launch(dispatcherProvider.io()) {
            when (stateEvent) {
                is GetEmployeesEvent -> {
                    repository.getEmployees(stateEvent.forced)
                        .transformStateEvent()
                }

                is GetEmployeesSortedByTeamEvent -> {
                    repository.getEmployeesSortedByTeam()
                        .transformStateEvent()

                }
                is GetEmployeesSortedByLastNameEvent -> {
                    repository.getEmployeesSortedByLastName()
                        .transformStateEvent()
                }
                is GetEmployeesSortedByFirstNameEvent -> {
                    repository.getEmployeesSortedByFirstName()
                            .transformStateEvent()
                }
                is FilterEmployeesByAnyEvent -> {
                    repository.filterByAny(stateEvent.searchTerm)
                            .transformStateEvent()
                }
                is GetEmployeeDetailEvent -> {
                    // Don't navigate if we're in landscape mode and the selected employee
                    // hasn't changed.
                    if (!stateEvent.isTablet || _currentEmployee.value != stateEvent.employee) {
                        _currentEmployee.postValue(stateEvent.employee)
                        _oneTimeNavigateEvent.postValue(Event(stateEvent))
                    }
                }
            }
        }
    }

    @FlowPreview
    suspend fun Flow<DataState<List<Employee>>>.transformStateEvent() =
        this.debounce(500)
                .distinctUntilChanged()
                .onEach { newDataState ->
                    withContext(dispatcherProvider.main()) {
                        _dataState.value = newDataState
                    }
                }.collect()
}