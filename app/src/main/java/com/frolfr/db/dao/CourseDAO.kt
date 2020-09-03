package com.frolfr.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frolfr.db.model.CourseEntity
import com.frolfr.domain.model.ScorecardStats

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

    @Query("""
        SELECT
            COUNT(r.id) AS completedScorecards,
            AVG(s.score) AS averageScore,
            MIN(s.score) AS bestScore,
            AVG(s.strokes) AS averageStrokes,
            MIN(s.strokes) AS bestStrokes
        FROM Course c
        JOIN Round r ON r.courseId = c.id
        JOIN UserScorecard s ON s.roundId = r.id AND s.isComplete = 1
        WHERE c.id = :courseId
        GROUP BY r.id
    """)
    fun getScorecardStats(courseId: Int): LiveData<ScorecardStats>
}