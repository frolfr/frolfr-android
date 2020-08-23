package com.frolfr.db;

import androidx.room.Embedded
import androidx.room.Relation

data class UserScorecardEntityWithUser(
    @Embedded
    val userScorecard: UserScorecardEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    val user: UserEntity
)
