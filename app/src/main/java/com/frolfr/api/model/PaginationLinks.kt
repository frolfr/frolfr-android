package com.frolfr.api.model

data class PaginationLinks(
    val first: String,
    val prev: String?,
    val next: String?,
    val last: String
) {
    fun hasNextPage(): Boolean {
        return next != null
    }
}