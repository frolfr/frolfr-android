package com.frolfr.api.mapper

import com.frolfr.domain.model.Turn

class TurnMapper : Mapper<com.frolfr.api.model.Turn, Turn> {

    override fun toDomain(turn: com.frolfr.api.model.Turn, vararg extras: Any): Turn {
        return Turn(
            turn.id.toInt(),
            extras[0] as Int,
            turn.holeNumber,
            turn.par,
            turn.strokes
        )
    }

}