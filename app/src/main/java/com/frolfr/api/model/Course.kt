package com.frolfr.api.model

import com.squareup.moshi.Json

data class Course(
    val id: Int,
    val city: String,
    val state: String,
    val country: String,
    val name: String,
    val status: String,
    val location: String,
    @Json(name = "photo_ids") val photoIds: List<Int>,
    @Json(name = "last_played_at") val lastPlayedAt: String,
    @Json(name = "hole_ids") val holeIds: List<Int>,
    @Json(name = "scorecard_ids") val scorecardIds: List<Int>,
    @Json(name = "hole_count") val holeCount: Int
)