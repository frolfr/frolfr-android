package com.frolfr.api.model

import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import java.util.*

// TODO can we make this a data class?
@JsonApi(type = "rounds")
class Round : Resource() {
    /** @Json not working w/ the jsonapi library... */
    @field:Json(name = "created-at") lateinit var createdAt: Date

    private lateinit var users: HasMany<User2>
    private lateinit var course: HasOne<Course2>
    private lateinit var scorecards: HasMany<Scorecard2>

    private val userScoreMap = mutableMapOf<String, Int>()

    fun getUsers(): List<User2> {
        return users.get(document)
    }

    fun getCourse(): Course2 {
        return course.get(document)
    }

    fun getScorecards(): List<Scorecard2> {
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