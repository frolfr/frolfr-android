package com.frolfr.db.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserScorecard")
data class UserScorecardEntity(
    @PrimaryKey
    var id: String = "",
    var roundId: Int = 0,
    var userId: Int = 0,
    var strokes: Int = 0,
    var score: Int = 0
)