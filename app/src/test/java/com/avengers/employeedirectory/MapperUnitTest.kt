package com.avengers.employeedirectory

import com.avengers.employeedirectory.db.CacheMapper
import com.avengers.employeedirectory.models.Employee
import com.avengers.employeedirectory.network.EmployeeNetworkEntity
import com.avengers.employeedirectory.network.NetworkMapper
import com.avengers.employeedirectory.sqldelight.EmployeeCacheEntity
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MapperUnitTest {
    companion object {
        private const val fullName = "Brady Aiello"
        private const val firstName = "Brady"
        private const val lastName = "Aiello"
        private const val employeeType = "FULL_TIME"
        private const val team = "Special Ops"
        private const val id = "12345"
        private const val email = "brady@avengers.org"
        private const val photoSmall = "somephotourl1.com"
        private const val photoLarge = "somephotourl2.com"
        private const val phone = "0123456789"
        private const val biography = "I love Android, surfing, and medieval doom metal"

        private const val fullNameCap = "Steve Rogers"
        private const val firstNameCap = "Steve"
        private const val lastNameCap = "Rogers"
        private const val employeeTypeCap = "FULL_TIME"
        private const val teamCap = "Leadership"
        private const val idCap = "01234"
        private const val emailCap = "cap@avengers.org"
        private const val photoSmallCap = "somecapphotourl1.com"
        private const val photoLargeCap = "somecapphotourl2.com"
        private const val phoneCap = "1234567890"
        private const val biographyCap = "I can do this all day."

        private val employeeBrady = Employee(biography, email, employeeType,
                firstName, lastName, phone, photoLarge, photoSmall, team, id)
        private val employeeNetworkBrady = EmployeeNetworkEntity(biography, email, employeeType,
                fullName, phone, photoLarge, photoSmall, team, id)
        private val employeeCacheBrady = EmployeeCacheEntity(biography, email, employeeType,
                firstName, lastName, phone, photoLarge, photoSmall, team, id)

        private val employeeCap = Employee(biographyCap, emailCap, employeeTypeCap,
                firstNameCap, lastNameCap, phoneCap, photoLargeCap, photoSmallCap, teamCap, idCap)
        private val employeeCacheCap = EmployeeCacheEntity(biographyCap, emailCap, employeeTypeCap,
                firstNameCap, lastNameCap, phoneCap, photoLargeCap, photoSmallCap, teamCap, idCap)
        private val employeeNetworkCap = EmployeeNetworkEntity(biographyCap, emailCap, employeeTypeCap,
                fullNameCap, phoneCap, photoLargeCap, photoSmallCap, teamCap, idCap)

        private val employees = listOf<Employee>(employeeBrady, employeeCap)
        private val employeesNetwork = listOf<EmployeeNetworkEntity>(employeeNetworkBrady, employeeNetworkCap)
        private val employeesCache = listOf<EmployeeCacheEntity>(employeeCacheBrady, employeeCacheCap)

    }

    private lateinit var networkMapper: NetworkMapper
    private lateinit var cacheMapper: CacheMapper

    @Before
    fun setup() {
        networkMapper = NetworkMapper()
        cacheMapper = CacheMapper()
    }

    @Test
    fun networkEmployeeToEmployeeTest() {
        val answerEmployee: Employee = networkMapper.mapFromEntity(employeeNetworkBrady)
        assertEquals(employeeBrady, answerEmployee)
    }

    @Test
    fun employeeToNetworkEmployeeTest() {
        val networkEmployeeAnswer: EmployeeNetworkEntity = networkMapper.mapToEntity(employeeBrady)
        assertEquals(employeeNetworkBrady, networkEmployeeAnswer)
    }

    @Test
    fun cacheEmployeeToEmployeeTest() {
        val answerEmployee: Employee = cacheMapper.mapFromEntity(employeeCacheBrady)
        assertEquals(employeeBrady, answerEmployee)
    }

    @Test
    fun employeeToCacheEmployeeTest() {
        val networkEmployeeAnswer: EmployeeCacheEntity = cacheMapper.mapToEntity(employeeBrady)
        assertEquals(employeeCacheBrady, networkEmployeeAnswer)
    }

    @Test
    fun employeesNetworkToEmployees() {
        val employeesAnswer = networkMapper.mapFromEntities(employeesNetwork)
        assertEquals(employees, employeesAnswer)
    }

    @Test
    fun employeesToEmployeesNetwork() {
        val employeesNetworkAnswer = networkMapper.mapToEntities(employees)
        assertEquals(employeesNetwork, employeesNetworkAnswer)
    }

    @Test
    fun employeesCacheToEmployees() {
        val employeesAnswer = cacheMapper.mapFromEntities(employeesCache)
        assertEquals(employees, employeesAnswer)
    }

    @Test
    fun employeesToEmployeesCache() {
        val employeesCacheAnswer = cacheMapper.mapToEntities(employees)
        assertEquals(employeesCache, employeesCacheAnswer)
    }

    fun String.studliest(): String {
        val stringBuilder = StringBuilder()
        this.forEachIndexed { index, c ->
            stringBuilder.append(if (index % 2 == 0) c.toLowerCase() else c.toUpperCase())}
        return stringBuilder.toString()
    }

    private fun String.studly(): String {
        return this.mapIndexed{ index, c ->
            (if (index % 2 == 0) c.toLowerCase() else c.toUpperCase())}
            .joinToString("")
    }

    @Test
    fun studlyTest() {
        val hello = "hello".studliest()
    }
}