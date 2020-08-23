package com.frolfr.db

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface CourseDAO {
    @Insert
    fun insert(course: CourseEntity)
}