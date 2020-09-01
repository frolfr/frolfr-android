package com.frolfr.ui.courses

import com.frolfr.domain.model.Course
import java.util.*

data class CourseWithLastPlayed (
    val course: Course,
    val lastPlayed: Date?
)