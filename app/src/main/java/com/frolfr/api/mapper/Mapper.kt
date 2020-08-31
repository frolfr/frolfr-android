package com.frolfr.api.mapper

/**
 * M Model (API Model)
 * D Domain
 */
interface Mapper<M, D> {
    fun toDomain(apiModel: M, vararg extras: Any): D
}