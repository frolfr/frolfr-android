package com.frolfr.db;

import androidx.room.Embedded
import androidx.room.Relation

data class RoundWithRelations(
    @Embedded
    val round: RoundEntity,
    @Relation(
        parentColumn = "courseId",
        entityColumn = "id"
    )
    val course: CourseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "roundId"
    )
    val userScorecards: List<UserScorecardEntityWithUser>
)
