package com.frolfr.api.model

import com.squareup.moshi.Json

data class UserScorecardSummary(
    val id: Int,
    @Json(name = "total_strokes") val strokes: Int,
    @Json(name = "total_score") val score: Int,
    @Json(name = "created_at") val date: String,
    @Json(name = "round_id") val roundId: Int,
    @Json(name = "is_completed") val complete: Boolean,
    val rating: Double?
)