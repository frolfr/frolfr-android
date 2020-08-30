package com.frolfr.db.model;

import androidx.room.Embedded
import androidx.room.Relation

data class UserScorecardEntityWithRelations(
    @Embedded
    var userScorecard: UserScorecardEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    var user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userScorecardId"
    )
    var turns: List<TurnEntity>
) {
    constructor() : this(UserScorecardEntity(), UserEntity(), emptyList())
}
