package com.avengers.employeedirectory.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EmployeesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employeeCacheEntity: EmployeeCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(employeeCacheEntities: List<EmployeeCacheEntity>)

    @Query("SELECT * FROM employees ORDER BY team")
    suspend fun getEmployeesSortedByTeam(): List<EmployeeCacheEntity>

    @Query("SELECT * FROM employees ORDER BY lastName ASC")
    suspend fun getEmployeesSortedByLastName(): List<EmployeeCacheEntity>

    @Query("SELECT * FROM employees ORDER BY firstName ASC")
    suspend fun getEmployeesSortedByFirstName(): List<EmployeeCacheEntity>

    @Query("SELECT * FROM employees WHERE firstName LIKE :searchTerm OR lastName LIKE :searchTerm OR team LIKE :searchTerm OR emailAddress like :searchTerm OR employeeType LIKE :searchTerm OR biography  LIKE :searchTerm ORDER BY lastName ASC")
    suspend fun filterEmployeesByAny(searchTerm: String): List<EmployeeCacheEntity>

    @Query("SELECT * FROM employees WHERE uuid = :uuid")
    suspend fun getEmployee(uuid: String): EmployeeCacheEntity
}
