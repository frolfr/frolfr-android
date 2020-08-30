package com.frolfr.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.frolfr.db.model.RoundEntity
import com.frolfr.db.model.RoundWithRelations

@Dao
interface RoundDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(round: RoundEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(rounds: List<RoundEntity>)

    @Transaction
    @Query("SELECT * FROM Round WHERE id = :id")
    fun get(id: Int): RoundWithRelations?

    @Query("SELECT * FROM Round ORDER BY createdAt DESC")
    fun getAllRounds(): LiveData<List<RoundEntity>>

    // TODO only grab rounds from the current day
    @Query("SELECT * FROM Round WHERE isComplete = 0 ORDER BY createdAt DESC LIMIT 1")
    fun getCurrentRound(): RoundEntity?

    @Transaction
    @Query("SELECT * FROM Round ORDER BY createdAt DESC")
    fun getAllRoundsFull(): LiveData<List<RoundWithRelations>>
}