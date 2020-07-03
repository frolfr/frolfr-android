package com.frolfr.api.model

import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import kotlin.properties.Delegates

@JsonApi(type = "courses")
class Course2 : Resource() {
    lateinit var name: String
    @field:Json(name = "holes-count") var holeCount = 18
    lateinit var city: String
    lateinit var state: String
    lateinit var country: String

    fun getLocation(): String {
        return "$city, $state"
    }
}