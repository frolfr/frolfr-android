package com.frolfr.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.frolfr.db.model.CourseEntity

@Dao
interface CourseDAO {
    @Insert
    fun insert(course: CourseEntity)
}