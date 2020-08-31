package com.frolfr.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.frolfr.db.model.TurnEntity

@Dao
interface TurnDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(turn: TurnEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(turns: List<TurnEntity>)
}