package com.frolfr.db.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Round")
data class RoundEntity(
    @PrimaryKey
    var id: Int = 0,
    var createdAt: Long = System.currentTimeMillis(),
    var courseId: Int = 0,
    var isComplete: Boolean = false
)
