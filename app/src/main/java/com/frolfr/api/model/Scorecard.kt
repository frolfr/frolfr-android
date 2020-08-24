package com.frolfr.api.model

import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import java.util.*

@JsonApi(type = "scorecards")
class Scorecard : Resource() {
    @field:Json(name = "created-at") lateinit var createdAt: Date
    @field:Json(name = "round-id") var roundId = 0

    private lateinit var turns: HasMany<Turn>
    private lateinit var user: HasOne<User>

    fun getTurns(): List<Turn> {
        return turns.get(document)
    }

    fun getUser(): User {
        return user.get(document)
    }
}