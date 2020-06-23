package com.frolfr.api.model

import com.squareup.moshi.Json

data class ScorecardResponse(
    @Json(name = "round") val roundInfo: ScorecardMeta,
    @Json(name = "scorecards") val userScorecards: List<UserScorecard>,
    @Json(name = "turns") val holeResults: List<HoleResult>
)

data class ScorecardMeta(
    val id: Int,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "course_id") val courseId: Int,
    @Json(name = "course_name") val courseName: String,
    @Json(name = "hole_count") val holeCount: Int,
    @Json(name = "public_recap") val publicRecap: Boolean,
    @Json(name = "scorecard_ids") val scorecardIds: List<Int>
)

data class UserScorecard(
    val id: Int,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "course_id") val courseId: Int,
    @Json(name = "course_name") val courseName: String,
    @Json(name = "round_id") val roundId: Int,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "user_initials") val userInitials: String,
    @Json(name = "turn_ids") val turnIds: List<Int>
)

data class HoleResult(
    val id: Int,
    val strokes: Int,
    val par: Int,
    @Json(name = "hole_number") val hole: Int,
    @Json(name = "scorecard_id") val userScorecardId: Int,
    @Json(name = "hole_id") val holeId: Int
)