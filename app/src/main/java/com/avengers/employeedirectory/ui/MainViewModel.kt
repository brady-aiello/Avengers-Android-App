package com.avengers.employeedirectory.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.employeedirectory.async.DispatcherProvider
import com.avengers.employeedirectory.models.Employee
import com.avengers.employeedirectory.repository.DefaultEmployeeRepository
import com.avengers.employeedirectory.repository.EmployeeRepository
import com.avengers.employeedirectory.util.DataState
import com.avengers.employeedirectory.util.EmployeesStateEvent
import com.avengers.employeedirectory.util.EmployeesStateEvent.*
import com.avengers.employeedirectory.util.Event
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


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
                        .debounce(500)
                        .onEach { newDataState ->
                            if (_dataState.value != newDataState) {
                                _dataState.value = newDataState
                            }
                        }
                        .launchIn(viewModelScope)
                }

                is GetEmployeesSortedByTeamEvent -> {
                    repository.getEmployeesSortedByTeam()
                        .debounce(500)
                        .onEach { newDataState ->
                            if (_dataState.value != newDataState) {
                                _dataState.value = newDataState
                            }
                        }
                        .launchIn(viewModelScope)
                }
                is GetEmployeesSortedByLastNameEvent -> {
                    repository.getEmployeesSortedByLastName()
                        .debounce(500)
                        .onEach { newDataState ->
                            if (_dataState.value != newDataState) {
                                _dataState.value = newDataState
                            }
                        }
                        .launchIn(viewModelScope)
                }
                is GetEmployeesSortedByFirstNameEvent -> {
                    repository.getEmployeesSortedByFirstName()
                        .debounce(500)
                        .onEach { newDataState ->
                            if (_dataState.value != newDataState) {
                                _dataState.value = newDataState
                            }
                        }
                        .launchIn(viewModelScope)
                }
                is FilterEmployeesByAnyEvent -> {
                    repository.filterByAny(stateEvent.searchTerm)
                        .debounce(500)
                        .onEach { newDataState ->
                            if (_dataState.value != newDataState) {
                                _dataState.value = newDataState
                            }
                        }
                        .launchIn(viewModelScope)
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
}