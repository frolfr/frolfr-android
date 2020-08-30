package com.frolfr.domain.model

class UserScorecard (
    val id: Int,
    val roundId: Int,
    val user: User,
    val strokes: Int,
    val score: Int,
    val turns: List<Turn>
)