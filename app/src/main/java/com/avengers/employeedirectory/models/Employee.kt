package com.avengers.employeedirectory.models

// EmployeeCacheEntity: DB domain model
// EmployeeNetworkEntity: network domain model
// Employee: In memory domain model
data class Employee(
    val biography: String,
    val emailAddress: String,
    val employeeType: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val photoUrlLarge: String,
    val photoUrlSmall: String,
    val team: String,
    val uuid: String
)