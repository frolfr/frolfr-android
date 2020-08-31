package com.frolfr.db.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Turn")
data class TurnEntity(
    @PrimaryKey
    var id: Int = 0,
    var userScorecardId: Int = 0,
    var holeNumber: Int = 0,
    var par: Int = 0,
    var strokes: Int? = null
)
