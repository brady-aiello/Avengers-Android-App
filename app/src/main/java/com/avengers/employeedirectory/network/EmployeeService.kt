package com.avengers.employeedirectory.network

import retrofit2.http.GET

interface EmployeeService {
    @GET("avengers.json")
    suspend fun getEmployees(): EmployeesNetworkEntity

    @GET("avengers_malformed.json")
    suspend fun getEmployeesMalformed(): EmployeesNetworkEntity

    @GET("avengers_empty.json")
    suspend fun getEmployeesEmpty(): EmployeesNetworkEntity
}