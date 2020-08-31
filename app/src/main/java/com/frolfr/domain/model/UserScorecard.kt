package com.frolfr.domain.model

data class UserScorecard (
    val id: String,
    val roundId: Int,
    val user: User,
    val strokes: Int,
    val score: Int,
    val turns: List<Turn>
)