package com.frolfr.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.frolfr.db.model.UserScorecardEntity

@Dao
interface UserScorecardDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userScorecard: UserScorecardEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userScorecards: List<UserScorecardEntity>)
}