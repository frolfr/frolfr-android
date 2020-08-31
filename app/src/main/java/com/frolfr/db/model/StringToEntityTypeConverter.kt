package com.frolfr.db.model

import androidx.room.TypeConverter

class StringToEntityTypeConverter {
    @TypeConverter
    fun stringToEntityType(value: String?): EntityType? {
        return value?.let { EntityType.valueOf(value) }
    }
}