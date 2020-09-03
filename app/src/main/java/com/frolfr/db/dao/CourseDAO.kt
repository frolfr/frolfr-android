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
            SUM(s.isComplete) AS completedScorecards,
            SUM(CASE WHEN s.isComplete THEN 0 ELSE 1 END) AS incompleteScorecards,
            AVG(s.score) AS averageScore,
            MIN(s.score) AS bestScore,
            AVG(s.strokes) AS averageStrokes,
            MIN(s.strokes) AS bestStrokes
        FROM Course c
        JOIN Round r ON r.courseId = c.id
        JOIN UserScorecard s ON s.roundId = r.id
        WHERE c.id = :courseId AND (s.userId = :userId OR :userId IS NULL)
        GROUP BY c.id
    """)
    fun getScorecardStats(courseId: Int, userId: Int?): LiveData<ScorecardStats>
}