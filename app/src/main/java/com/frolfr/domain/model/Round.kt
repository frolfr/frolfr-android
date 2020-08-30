package com.frolfr.domain.model

import java.util.*

data class Round (
    val id: Int,
    val createdAt: Date,
    val course: Course,
    val userScorecards: List<UserScorecard>,
    val isComplete: Boolean
)