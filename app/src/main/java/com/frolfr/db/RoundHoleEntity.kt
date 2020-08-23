package com.frolfr.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RoundHole")
data class RoundHoleEntity(
    @PrimaryKey
    var id: Int = 0,
    var roundId: Int = 0,
    var holeNumber: Int = 0,
    var par: Int = 0
)
