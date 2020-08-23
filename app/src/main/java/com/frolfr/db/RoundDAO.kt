package com.frolfr.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RoundDAO {
    @Insert
    fun insert(round: RoundEntity)

    @Transaction
    @Query("SELECT * FROM Round WHERE id = :id")
    fun get(id: Int): RoundWithRelations?

    @Query("SELECT * FROM Round ORDER BY createdAt DESC")
    fun getAllRounds(): LiveData<List<RoundEntity>>

    // TODO only grab rounds from the current day
    @Query("SELECT * FROM Round WHERE isComplete = 0 ORDER BY createdAt DESC LIMIT 1")
    fun getCurrentRound(): RoundEntity?

    @Transaction
    @Query("SELECT * FROM Round")
    fun getAllRoundsFull(): LiveData<List<RoundWithRelations>>
}