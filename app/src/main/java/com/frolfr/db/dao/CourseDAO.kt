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

    @Query("SELECT * FROM Course ORDER BY name ASC")
    fun getAllLive(): LiveData<List<CourseEntity>>

    @Query("SELECT * FROM Course ORDER BY name ASC")
    fun getAll(): List<CourseEntity>

    @Query("SELECT * FROM Course WHERE id = :courseId")
    fun get(courseId: Int): LiveData<CourseEntity>
}