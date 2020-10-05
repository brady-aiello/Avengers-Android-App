package com.avengers

import com.avengers.employeedirectory.db.CacheMapper
import com.avengers.employeedirectory.sqldelight.EmployeeCacheEntity
import com.avengers.employeedirectory.models.Employee
import com.avengers.employeedirectory.network.NetworkMapper

val testEmployees = listOf(
    Employee(
        uuid = "0d8fcc12-4d0c-425c-8355-390b312b909c",
        firstName = "Thor",
        lastName = "Odinson",
        phoneNumber = "5553280123",
        emailAddress = "thor@avengers.org",
        biography = "Fortunately, I am mighty.",
        photoUrlSmall = "https://img1.looper.com/img/gallery/the-entire-thor-mcu-story-finally-explained/intro-1564416884.jpg",
        photoUrlLarge = "https://img1.looper.com/img/gallery/the-entire-thor-mcu-story-finally-explained/intro-1564416884.jpg",
        team = "Demolition",
        employeeType = "FULL_TIME"
    ),

    Employee(
        uuid = "a98f8a2e-c975-4ba3-8b35-01f719e7de2d",
        firstName = "Tony",
        lastName = "Stark",
        phoneNumber = "5558531970",
        emailAddress = "stark@avengers.org",
        biography = "I am Iron Man.",
        photoUrlSmall = "https://upload.wikimedia.org/wikipedia/en/thumb/c/cb/Robert_Downey_Jr._as_Iron_Man_in_Avengers_Infinity_War.jpg/220px-Robert_Downey_Jr._as_Iron_Man_in_Avengers_Infinity_War.jpg",
        photoUrlLarge = "https://upload.wikimedia.org/wikipedia/en/thumb/c/cb/Robert_Downey_Jr._as_Iron_Man_in_Avengers_Infinity_War.jpg/220px-Robert_Downey_Jr._as_Iron_Man_in_Avengers_Infinity_War.jpg",
        team = "Research and Development",
        employeeType = "FULL_TIME"
    ),
    Employee(
        uuid = "61b21d34-5499-401a-98b3-16f26e645d54",
        firstName =  "Natasha",
        lastName = "Romanov",
        phoneNumber = "5555442937",
        emailAddress = "widow@avengers.org",
        biography = "Got a lot of red on my ledger.",
        photoUrlSmall = "https://movies-b26f.kxcdn.com/wp-content/uploads/2019/12/Screen-Shot-2019-12-03-at-12.38.13-AM-770x470.png",
        photoUrlLarge = "https://movies-b26f.kxcdn.com/wp-content/uploads/2019/12/Screen-Shot-2019-12-03-at-12.38.13-AM-770x470.png",
        team = "Special Ops",
        employeeType = "FULL_TIME"
    )
)
private val cacheMapper = CacheMapper()
private val networkMapper = NetworkMapper()

val testCacheEmployees = cacheMapper.mapToEntities(testEmployees)
val testNetworkEmployees = networkMapper.mapToEntities(testEmployees)


fun String.like(term: String): Boolean {
    return this.toLowerCase().contains(term.toLowerCase())
}

fun List<EmployeeCacheEntity>.filterByAny(term: String) = filter {
    it.biography.like(term) ||
    it.emailAddress.like(term) ||
    it.employeeType.like(term) ||
    it.firstName.like(term) ||
    it.lastName.like(term)
}

fun List<Employee>.filterByAnyTerm(term: String) = filter {
    it.biography.like(term) ||
    it.emailAddress.like(term) ||
    it.employeeType.like(term) ||
    it.firstName.like(term) ||
    it.lastName.like(term)
}