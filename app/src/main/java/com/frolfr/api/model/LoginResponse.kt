package com.frolfr.api.model

import com.squareup.moshi.Json

data class LoginResponse(@Json(name = "access_token") val token: String)