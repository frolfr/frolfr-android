package com.frolfr.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.frolfr.db.model.CourseEntity

@Dao
interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(course: CourseEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(courses: List<CourseEntity>)
}