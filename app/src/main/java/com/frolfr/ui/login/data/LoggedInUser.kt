package com.frolfr.ui.login.data

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val id: Int,
    val email: String,
    val token: String,
    val name: String,
    val avatarUrl: String?
)
