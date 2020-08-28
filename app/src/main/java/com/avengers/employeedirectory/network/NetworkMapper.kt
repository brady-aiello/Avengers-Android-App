package com.avengers.employeedirectory.network

import com.avengers.employeedirectory.models.Employee
import com.avengers.employeedirectory.util.EntityMapper
import javax.inject.Inject

class NetworkMapper @Inject constructor(): EntityMapper<EmployeeNetworkEntity, Employee>() {
    override fun mapFromEntity(entity: EmployeeNetworkEntity): Employee {
        val (first, last) = entity.fullName.split(" ")
        return Employee(
            entity.biography, entity.emailAddress, entity.employeeType,
            first, last, entity.phoneNumber, entity.photoUrlLarge,
            entity.photoUrlSmall, entity.team, entity.uuid
        )
    }

    override fun mapToEntity(domainModel: Employee): EmployeeNetworkEntity =
        EmployeeNetworkEntity(
            domainModel.biography,
            domainModel.emailAddress,
            domainModel.employeeType,
            "${domainModel.firstName} ${domainModel.lastName}",
            domainModel.phoneNumber,
            domainModel.photoUrlLarge,
            domainModel.photoUrlSmall,
            domainModel.team,
            domainModel.uuid
        )

}