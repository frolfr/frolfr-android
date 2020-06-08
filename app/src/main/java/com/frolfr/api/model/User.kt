package com.frolfr.api.model

import com.squareup.moshi.Json

data class User(
    val id: Int,
    val email: String,
    @Json(name = "first_name") val nameFirst: String,
    @Json(name = "middle_name") val nameMiddle: String?,
    @Json(name = "last_name") val nameLast: String?,
    @Json(name = "avatar_url") val avatarUrl: String?
)