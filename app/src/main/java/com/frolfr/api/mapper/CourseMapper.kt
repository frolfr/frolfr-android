package com.frolfr.api.mapper

import com.frolfr.domain.model.Course

class CourseMapper : Mapper<com.frolfr.api.model.Course, Course> {

    override fun toDomain(course: com.frolfr.api.model.Course, vararg extras: Any): Course {
        return Course(
            course.id.toInt(),
            course.name,
            course.holeCount,
            course.city,
            course.state,
            course.country
        )
    }

}