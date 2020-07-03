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

    fun getUsers(): List<User2> {
        return users.get(document)
    }

    fun getCourse(): Course2 {
        return course.get(document)
    }
}