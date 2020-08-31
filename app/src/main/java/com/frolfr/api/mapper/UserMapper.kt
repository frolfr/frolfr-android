package com.frolfr.api.mapper

import com.frolfr.domain.model.User

class UserMapper : Mapper<com.frolfr.api.model.User, User> {

    override fun toDomain(user: com.frolfr.api.model.User, vararg extras: Any): User {
        return User(
            user.id.toInt(),
            user.email,
            user.firstName,
            user.lastName,
            user.avatarUrl
        )
    }

}