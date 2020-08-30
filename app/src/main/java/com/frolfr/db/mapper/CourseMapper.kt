package com.frolfr.db.mapper

import com.frolfr.db.model.CourseEntity
import com.frolfr.domain.model.Course

class CourseMapper : Mapper<CourseEntity, Course> {
    override fun fromModel(course: CourseEntity): Course {
        return Course(
            course.id,
            course.name,
            course.holeCount,
            course.city,
            course.state,
            course.country
        )
    }

    override fun toModel(course: Course): CourseEntity {
        return CourseEntity(
            course.id,
            course.name,
            course.holeCount,
            course.city,
            course.state,
            course.country
        )
    }
}