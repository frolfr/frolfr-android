package com.frolfr.db.mapper

import com.frolfr.db.model.TurnEntity
import com.frolfr.db.model.UserEntity
import com.frolfr.domain.Turn
import com.frolfr.domain.User

class TurnMapper : Mapper<TurnEntity, Turn> {
    override fun fromModel(turn: TurnEntity): Turn {
        return Turn(
            turn.id,
            turn.userScorecardId,
            turn.par,
            turn.strokes
        )
    }

    override fun toModel(turn: Turn): TurnEntity {
        TODO("Not yet implemented")
    }
}