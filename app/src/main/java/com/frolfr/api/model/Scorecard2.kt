package com.frolfr.api.model

import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import java.util.*
import kotlin.properties.Delegates

@JsonApi(type = "scorecards")
class Scorecard2 : Resource() {
    @field:Json(name = "created-at") lateinit var createdAt: Date
    @field:Json(name = "round-id") var roundId = 0

    private lateinit var turns: HasMany<Turn2>
    private lateinit var user: HasOne<User2>

    fun getTurns(): List<Turn2> {
        return turns.get(document)
    }

    fun getUser(): User2 {
        return user.get(document)
    }
}