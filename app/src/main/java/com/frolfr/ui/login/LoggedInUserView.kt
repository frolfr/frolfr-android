package com.frolfr.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val id: Int,
    val email: String,
    val name: String,
    val avatarUrl: String?
    //... other data fields that may be accessible to the UI
)
