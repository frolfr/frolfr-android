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
}