package com.frolfr.api.model

import com.squareup.moshi.Json
import moe.banana.jsonapi2.*
import java.util.*

// TODO can we make this a data class?
@JsonApi(type = "rounds")
class Round : Resource() {
    /** @Json not working w/ the jsonapi library... */
    @field:Json(name = "created-at") lateinit var createdAt: Date

    private lateinit var users: HasMany<User>
    private lateinit var course: HasOne<Course>
    private lateinit var scorecards: HasMany<Scorecard>

    @Transient
    private val userScoreMap = mutableMapOf<String, Int>()

    fun getCourse(): Course {
        return course.get(document)
    }

    fun setCourse(course: Course) {
        this.course = HasOne("courses", course.id)
    }

    fun getUsers(): List<User> {
        return users.get(document)
    }

    fun setUsers(users: List<User>) {
        val userResourceIdentifiers = users.map { user ->
            ResourceIdentifier("users", user.id)
        }
        this.users = HasMany(*userResourceIdentifiers.toTypedArray())
    }

    fun getScorecards(): List<Scorecard> {
        return scorecards.get(document)
    }

    fun getUserScore(userId: String): Int {
        return userScoreMap.computeIfAbsent(userId) { key ->
            computeUserScore(key)
        }
    }

    private fun computeUserScore(userId: String): Int {
        val userScorecard = getScorecards().find { scorecard ->
            scorecard.getUser().id == userId
        }
        var score = 0
        for (turn in userScorecard?.getTurns() ?: emptyList()) {
            if (turn.isComplete()) {
                score += turn.getScore()
            }
        }
        return score
    }
}