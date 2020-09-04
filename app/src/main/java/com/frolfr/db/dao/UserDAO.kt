package com.frolfr.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frolfr.db.model.UserEntity

@Dao
interface UserDAO {
    @Query("SELECT * FROM User ORDER BY nameFirst ASC, nameLast ASC")
    fun getAll(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(users: List<UserEntity>)

    @Query("""
        SELECT * FROM User
        WHERE id IN (
            SELECT DISTINCT s.userId
            FROM Round r
            JOIN UserScorecard s ON s.roundId = r.id
            WHERE r.id IN (
                SELECT r.id
                FROM Round r
                JOIN UserScorecard s ON s.roundId = r.id
                WHERE s.userId = :userId
            ) AND s.userId <> :userId
        )
        ORDER BY nameFirst ASC, nameLast ASC
    """)
    fun getAllFriends(userId: Int): LiveData<List<UserEntity>>
}