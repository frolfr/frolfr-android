package com.frolfr.domain.model

data class ScorecardStats (
    val completedScorecards: Int,
    val averageScore: Float,
    val bestScore: Int,
    val averageStrokes: Float,
    val bestStrokes: Int
)