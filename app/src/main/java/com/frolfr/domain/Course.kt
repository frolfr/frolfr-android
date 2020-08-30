package com.frolfr.domain

data class Course (
    val id: Int,
    val name: String,
    val holeCount: Int,
    val city: String,
    val state: String,
    val country: String
) {
    fun getLocation(): String {
        return "$city, $state"
    }
}