package com.frolfr.api.mapper

import com.frolfr.domain.model.UserScorecard

class ScorecardMapper : Mapper<com.frolfr.api.model.Scorecard, UserScorecard> {

    private val userMapper = UserMapper()
    private val turnMapper = TurnMapper()

    override fun toDomain(scorecard: com.frolfr.api.model.Scorecard, vararg extras: Any): UserScorecard {
        val turns = scorecard.getTurns().map { turnMapper.toDomain(it, scorecard.id.toInt()) }
        val strokes = turns.map { turn -> turn.strokes ?: 0 }.reduce { acc, s -> acc.plus(s) }
        val score = turns.map { turn -> turn.getScore() }.reduce { acc, s -> acc.plus(s) }
        return UserScorecard(
            null,
            scorecard.roundId,
            userMapper.toDomain(scorecard.getUser()),
            strokes,
            score,
            turns
        )
    }

}