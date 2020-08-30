package com.frolfr.domain

data class User (
    val id: Int,
    val email: String,
    val nameFirst: String,
    val nameLast: String,
    val avatarUri: String
)