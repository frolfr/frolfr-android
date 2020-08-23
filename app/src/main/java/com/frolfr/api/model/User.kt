package com.frolfr.api.model

import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "users")
class User : Resource {
    lateinit var email: String
    @field:Json(name = "first-name") lateinit var firstName: String
    @field:Json(name = "last-name") var lastName: String? = null
    @field:Json(name = "avatar-url") var avatarUrl: String? = null

    constructor()

    constructor(id: String, email: String, firstName: String, lastName: String?, avatarUrl: String?) {
        this.id = id
        this.email = email
        this.firstName = firstName
        this.lastName = lastName
        this.avatarUrl = avatarUrl
    }

    fun getName(): String {
        if (lastName != null) {
            return "$firstName $lastName"
        }
        return firstName
    }
}