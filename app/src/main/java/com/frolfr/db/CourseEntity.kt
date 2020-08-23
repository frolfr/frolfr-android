package com.frolfr.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Course")
data class CourseEntity(
    @PrimaryKey
    var id: Int = 0,
    var name: String = "",
    var holeCount: Int = 18,
    var city: String = "",
    var state: String = "",
    var country: String = ""
)
