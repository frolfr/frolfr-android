package com.frolfr.api.model

import com.squareup.moshi.Json

data class ResponseMeta(
    @Json(name = "total_pages") val totalPages: Int
)