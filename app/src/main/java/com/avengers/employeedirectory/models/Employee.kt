package com.avengers.employeedirectory.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

// EmployeeCacheEntity: DB domain model
// EmployeeNetworkEntity: network domain model
// Employee: In memory domain model
@Serializable
@Parcelize
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
): Parcelable