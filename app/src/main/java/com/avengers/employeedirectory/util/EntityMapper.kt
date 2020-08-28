package com.avengers.employeedirectory.util

abstract class EntityMapper<Entity, DomainModel>() {
    abstract fun mapFromEntity(entity: Entity): DomainModel
    abstract fun mapToEntity(domainModel: DomainModel): Entity
    fun mapFromEntities(entities: List<Entity>): List<DomainModel> =
        entities.map { entity -> mapFromEntity(entity)}
    fun mapToEntities(domainModels: List<DomainModel>): List<Entity> =
        domainModels.map { domainModel -> mapToEntity(domainModel) }
}