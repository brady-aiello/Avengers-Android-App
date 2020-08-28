package com.avengers.employeedirectory.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class EmployeeCacheEntity(
    val biography: String,
    val emailAddress: String,
    val employeeType: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val photoUrlLarge: String,
    val photoUrlSmall: String,
    val team: String,
    @PrimaryKey(autoGenerate = false)
    val uuid: String
)
