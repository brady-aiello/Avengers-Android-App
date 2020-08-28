package com.avengers.employeedirectory.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EmployeeCacheEntity::class], version = 1)
abstract class EmployeeDatabase: RoomDatabase() {
    abstract fun employeesDao(): EmployeesDao
    companion object {
        const val DATABASE_NAME = "employee_db"
    }
}

