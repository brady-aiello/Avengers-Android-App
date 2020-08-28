package com.avengers.employeedirectory.network

import kotlinx.serialization.Serializable


@Serializable
data class EmployeesNetworkEntity(
    val employees: List<EmployeeNetworkEntity>
)