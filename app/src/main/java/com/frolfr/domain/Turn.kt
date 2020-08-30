package com.frolfr.domain

data class Turn (
    val id: Int,
    val userScorecardId: Int,
    val par: Int,
    val strokes: Int
) {
    fun getScore(): Int {
        return strokes - par
    }
}