package com.frolfr.api.model

import com.squareup.moshi.Json

data class UserCourseScorecardsResponse(
    @Json(name = "course_scorecards") val userScorecards: List<UserScorecard>
)