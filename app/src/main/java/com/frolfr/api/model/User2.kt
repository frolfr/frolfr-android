package com.frolfr.api.model

import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "users")
class User2 : Resource() {
    @field:Json(name = "first-name") lateinit var firstName: String
    @field:Json(name = "last-name") var lastName: String? = null
    lateinit var email: String
    @field:Json(name = "avatar-url") var avatarUrl: String? = null

    fun getName(): String {
        if (lastName != null) {
            return "$firstName $lastName"
        }
        return firstName
    }
}