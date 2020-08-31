package com.frolfr.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frolfr.db.model.CourseEntity

@Dao
interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(course: CourseEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(courses: List<CourseEntity>)

    @Query("SELECT * FROM Course")
    fun getAll(): LiveData<List<CourseEntity>>
}