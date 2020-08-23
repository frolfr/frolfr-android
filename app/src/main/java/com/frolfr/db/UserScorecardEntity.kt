package com.frolfr.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserScorecard")
data class UserScorecardEntity(
    @PrimaryKey
    var id: Int = 0,
    var roundId: Int = 0,
    var userId: Int = 0,
    var score: Int = 0
)
