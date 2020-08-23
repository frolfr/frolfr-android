package com.frolfr.db;

import androidx.room.Embedded
import androidx.room.Relation

data class UserScorecardEntityWithUser(
    @Embedded
    var userScorecard: UserScorecardEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    var user: UserEntity
) {
    constructor() : this(UserScorecardEntity(), UserEntity())
}
