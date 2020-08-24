package com.frolfr.api.model

import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import java.util.*
import kotlin.properties.Delegates

@JsonApi(type = "turns")
class Turn : Resource() {
    @field:Json(name = "hole-number") var holeNumber = 0
    @field:Json(name = "par") var par = 0
    @field:Json(name = "strokes") var strokes: Int? = null

    fun isComplete(): Boolean {
        return strokes != null && strokes!! > 0
    }

    fun getScore(): Int {
        return when (strokes) {
            null -> 0
            else -> strokes!! - par
        }
    }
}