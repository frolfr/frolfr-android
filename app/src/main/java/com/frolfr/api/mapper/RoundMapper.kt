package com.frolfr.api.mapper

import com.frolfr.api.mapper.CourseMapper
import com.frolfr.domain.model.Course
import com.frolfr.domain.model.Round

class RoundMapper : Mapper<com.frolfr.api.model.Round, Round> {

    // TODO
    private var courseMapper = CourseMapper()
//    private var userScorecardMapper = UserScorecardMapper()

    override fun toDomain(round: com.frolfr.api.model.Round): Round {
        return Round(
            round.id.toInt(),
            round.createdAt,
            // TODO
            courseMapper.toDomain(round.getCourse()),
            emptyList(),
//            round.getCourse(),
//            round.getScorecards().map {
//                userScorecardMapper.fromModel(it)
//            },
            true // TODO
        )
    }

}