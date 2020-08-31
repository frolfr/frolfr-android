package com.frolfr.domain.model

data class Turn (
    val id: Int,
    val userScorecardId: Int,
    val holeNumber: Int,
    val par: Int,
    val strokes: Int?
) {
    fun getScore(): Int {
        return strokes?.minus(par) ?: 0
    }
}