package com.frolfr.db.model

import androidx.room.TypeConverter

class EntityTypeToStringConverter {
    @TypeConverter
    fun entityTypeToString(value: EntityType?): String? {
        return value?.name
    }
}