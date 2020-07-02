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
    @field:Json(name = "created-at") lateinit var createdAt: Date
    // NOTE: @Json not working, need to use @field:Json for now
//    @Json(name = "created-at") var createdAt: String? = null
//    var `created-at`: String? = null
    lateinit var users: HasMany<User2>
    lateinit var course: HasOne<Course2>
}