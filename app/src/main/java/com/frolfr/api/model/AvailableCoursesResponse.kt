package com.frolfr.api.model

import com.squareup.moshi.Json

data class AvailableCoursesResponse(@Json(name = "available_courses") val courses: List<Course>)