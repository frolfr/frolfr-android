package com.frolfr.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
data class UserEntity(
    @PrimaryKey
    var id: Int = 0,
    var nameFirst: String = "",
    var nameLast: String = "",
    var email: String = "",
    var avatarUrl: String = ""
)
