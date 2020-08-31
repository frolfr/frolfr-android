package com.frolfr.db.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters

@Entity(tableName = "ApiSync")
data class ApiSyncEntity(
    @PrimaryKey
    @field:TypeConverters(EntityTypeToStringConverter::class, StringToEntityTypeConverter::class)
    var entityType: EntityType = EntityType.COURSE,
    var lastSyncedAt: Long = 0,
    var maxId: Int = 0,
    var minId: Int = Int.MAX_VALUE,
    var hasFullHistory: Boolean = false
)
