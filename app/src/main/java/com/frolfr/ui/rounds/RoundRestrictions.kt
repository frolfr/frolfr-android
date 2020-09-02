package com.frolfr.ui.rounds

import com.frolfr.api.FrolfrAuthorization

class RoundRestrictions {

    var userId: Int? = null
    var courseId: Int? = null

    fun withUser(userId: Int): RoundRestrictions {
        this.userId = userId
        return this
    }

    fun withCurrentUser(): RoundRestrictions {
        return withUser(FrolfrAuthorization.userId!!)
    }

    fun withCourse(courseId: Int): RoundRestrictions {
        this.courseId = courseId
        return this
    }

    override fun toString(): String {
        return "User[$userId]Course[$courseId]"
    }
}