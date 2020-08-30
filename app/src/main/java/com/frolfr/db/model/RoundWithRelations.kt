package com.frolfr.db.model;

import androidx.room.Embedded
import androidx.room.Relation

data class RoundWithRelations(
    @Embedded
    var round: RoundEntity,
    @Relation(
        parentColumn = "courseId",
        entityColumn = "id"
    )
    var course: CourseEntity,
    @Relation(
        parentColumn = "id",
        entity = UserScorecardEntity::class,
        entityColumn = "roundId"
    )
    var userScorecards: List<UserScorecardEntityWithRelations>
) {
    constructor() : this(RoundEntity(), CourseEntity(), emptyList<UserScorecardEntityWithRelations>())
}
