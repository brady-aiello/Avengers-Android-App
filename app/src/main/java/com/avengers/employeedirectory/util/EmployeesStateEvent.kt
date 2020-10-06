package com.avengers.employeedirectory.util

import com.avengers.employeedirectory.models.Employee

sealed class EmployeesStateEvent() {
    class GetEmployeesEvent(val forced: Boolean = false): EmployeesStateEvent()
    class FilterEmployeesByAnyEvent(val searchTerm: String): EmployeesStateEvent()
    object GetEmployeesSortedByTeamEvent: EmployeesStateEvent()
    object GetEmployeesSortedByLastNameEvent : EmployeesStateEvent()
    object GetEmployeesSortedByFirstNameEvent: EmployeesStateEvent()
}