package com.frolfr.api.mapper

import com.frolfr.domain.model.Round

class RoundMapper : Mapper<com.frolfr.api.model.Round, Round> {

    private var courseMapper = CourseMapper()
    private var scorecardMapper = ScorecardMapper()

    override fun toDomain(round: com.frolfr.api.model.Round, vararg extras: Any): Round {
        return Round(
            round.id.toInt(),
            round.createdAt,
            courseMapper.toDomain(round.getCourse()),
            round.getScorecards().map {
                scorecardMapper.toDomain(it)
            },
            true // TODO
        )
    }

}