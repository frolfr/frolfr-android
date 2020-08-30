package com.frolfr.db.mapper

import com.frolfr.db.model.UserEntity
import com.frolfr.domain.User

class UserMapper : Mapper<UserEntity, User> {
    override fun fromModel(user: UserEntity): User {
        return User(
            user.id,
            user.email,
            user.nameFirst,
            user.nameLast,
            user.avatarUrl
        )
    }

    override fun toModel(user: User): UserEntity {
        return UserEntity(
            user.id,
            user.nameFirst,
            user.nameLast,
            user.email,
            user.avatarUri
        )
    }
}