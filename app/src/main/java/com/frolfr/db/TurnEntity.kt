package com.frolfr.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Turn")
data class TurnEntity(
    @PrimaryKey
    var id: Int = 0,
    var roundHoleId: Int = 0,
    var userId: Int = 0,
    var strokes: Int = 0
)
