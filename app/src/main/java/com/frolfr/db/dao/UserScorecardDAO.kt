package com.frolfr.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frolfr.db.model.UserScorecardEntity

@Dao
interface UserScorecardDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userScorecard: UserScorecardEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userScorecards: List<UserScorecardEntity>)

    @Query("SELECT * FROM UserScorecard WHERE userId IN (:userIds) AND roundId IN (:roundIds)")
    fun getByUserAndRound(userIds: List<Int>, roundIds: List<Int>): List<UserScorecardEntity>
}