package com.frolfr.db.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
data class UserEntity(
    @PrimaryKey
    var id: Int = 0,
    var nameFirst: String = "",
    var nameLast: String? = null,
    var email: String = "",
    var avatarUrl: String? = null
)
