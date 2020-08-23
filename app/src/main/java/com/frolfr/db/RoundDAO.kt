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

    @Query("SELECT * FROM Round WHERE id = :id")
    fun get(id: Int): RoundEntity?

    @Query("SELECT * FROM Round ORDER BY createdAt DESC")
    fun getAllRounds(): LiveData<List<RoundEntity>>

    @Query("SELECT * FROM Round WHERE isComplete = 0 AND createdAt > date('now','-1 day') ORDER BY createdAt DESC LIMIT 1")
    fun getCurrentRound(): RoundEntity?

    @Transaction
    @Query("SELECT * FROM Round")
    fun getAllRoundsFull(): LiveData<List<RoundWithRelations>>
}