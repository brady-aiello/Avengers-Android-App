package com.avengers.employeedirectory.db

import com.avengers.employeedirectory.models.Employee
import com.avengers.employeedirectory.util.EntityMapper
import com.avengers.employeedirectory.sqldelight.EmployeeCacheEntity
import javax.inject.Inject

class CacheMapper @Inject constructor(): EntityMapper<EmployeeCacheEntity, Employee>() {

    override fun mapFromEntity(entity: EmployeeCacheEntity): Employee =
        Employee(entity.biography, entity.emailAddress, entity.employeeType,
            entity.firstName, entity.lastName, entity.phoneNumber, entity.photoUrlLarge,
            entity.photoUrlSmall, entity.team, entity.uuid)

    override fun mapToEntity(domainModel: Employee): EmployeeCacheEntity =
        EmployeeCacheEntity(domainModel.biography, domainModel.emailAddress,
            domainModel.employeeType, domainModel.firstName, domainModel.lastName, domainModel.phoneNumber,
            domainModel.photoUrlLarge, domainModel.photoUrlSmall, domainModel.team,
            domainModel.uuid)

}