package com.frolfr.db.mapper

/**
 * M Model (Database Model)
 * D Domain
 */
interface Mapper<M, D> {
    fun fromModel(type: M): D
    fun toModel(type: D): M
}