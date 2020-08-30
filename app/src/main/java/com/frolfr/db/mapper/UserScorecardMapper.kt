package com.frolfr.db.mapper

import com.frolfr.db.model.UserScorecardEntity
import com.frolfr.db.model.UserScorecardEntityWithRelations
import com.frolfr.domain.UserScorecard

class UserScorecardMapper : Mapper<UserScorecardEntityWithRelations, UserScorecard> {

    private var userMapper = UserMapper()
    private var turnMapper = TurnMapper()

    override fun fromModel(userScorecard: UserScorecardEntityWithRelations): UserScorecard {
        return UserScorecard(
            userScorecard.userScorecard.id,
            userScorecard.userScorecard.roundId,
            userMapper.fromModel(userScorecard.user),
            userScorecard.userScorecard.strokes,
            userScorecard.userScorecard.score,
            userScorecard.turns.map {
                turnMapper.fromModel(it)
            }
        )
    }

    override fun toModel(userScorecard: UserScorecard): UserScorecardEntityWithRelations {
        val user = userMapper.toModel(userScorecard.user)
        val userScorecardEntity = UserScorecardEntity(
            userScorecard.id,
            userScorecard.roundId,
            user.id,
            userScorecard.strokes,
            userScorecard.score
        )
        val turns = userScorecard.turns.map {
            turnMapper.toModel(it)
        }
        return UserScorecardEntityWithRelations(
            userScorecardEntity,
            user,
            turns
        )
    }
}