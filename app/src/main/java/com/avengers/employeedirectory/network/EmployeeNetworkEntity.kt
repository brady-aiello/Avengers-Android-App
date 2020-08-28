package com.avengers.employeedirectory.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeNetworkEntity(
    val biography: String,
    @SerialName("email_address") val emailAddress: String,
    @SerialName("employee_type") val employeeType: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("phone_number") val phoneNumber: String,
    @SerialName("photo_url_large") val photoUrlLarge: String,
    @SerialName("photo_url_small") val photoUrlSmall: String,
    val team: String,
    val uuid: String
)