package com.frolfr.db.dao

import androidx.room.*
import com.frolfr.db.model.UserScorecardEntity
import java.util.*

@Dao
interface UserScorecardDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userScorecard: UserScorecardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userScorecards: List<UserScorecardEntity>)

    @Query("""
        SELECT r.courseId, MAX(r.createdAt) as lastPlayed
        FROM UserScorecard s
        JOIN Round r ON s.roundId = r.id
        WHERE s.userId = :userId
        GROUP BY r.courseId
        """)
    fun getLastPlayedPerCourse(userId: Int): List<CourseLastPlayed>

    @Query("UPDATE UserScorecard SET isComplete = 1 WHERE roundId = :roundId")
    fun completeByRound(roundId: Int)
}

data class CourseLastPlayed(
    var courseId: Int,
    var lastPlayed: Long
)