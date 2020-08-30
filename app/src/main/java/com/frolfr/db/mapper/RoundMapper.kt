package com.frolfr.db.mapper

import com.frolfr.db.model.RoundEntity
import com.frolfr.db.model.RoundWithRelations
import com.frolfr.domain.Round
import java.util.*

class RoundMapper : Mapper<RoundWithRelations, Round> {

    private var courseMapper = CourseMapper()
    private var userScorecardMapper = UserScorecardMapper()

    override fun fromModel(round: RoundWithRelations): Round {
        return Round(
            round.round.id,
            Date(round.round.createdAt),
            courseMapper.fromModel(round.course),
            round.userScorecards.map {
                userScorecardMapper.fromModel(it)
            },
            round.round.isComplete
        )
    }

    override fun toModel(round: Round): RoundWithRelations {
        val roundWithRelations = RoundWithRelations()
        val course = courseMapper.toModel(round.course)
        val userScorecards = round.userScorecards.map {
            userScorecardMapper.toModel(it)
        }
        roundWithRelations.round = RoundEntity(
            round.id,
            round.createdAt.time,
            course.id,
            round.isComplete
        )
        roundWithRelations.course = course
        roundWithRelations.userScorecards = userScorecards
        return roundWithRelations
    }

}