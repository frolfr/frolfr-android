package com.frolfr.api.mapper

/**
 * M Model (API Model)
 * D Domain
 */
interface Mapper<M, D> {
    fun toDomain(type: M): D
}