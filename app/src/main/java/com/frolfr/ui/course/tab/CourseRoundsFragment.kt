package com.frolfr.ui.course.tab

import com.frolfr.ui.rounds.RoundRestrictions
import com.frolfr.ui.rounds.RoundsFragment

// TODO re-use the existing RoundsFragment but extend it or otherwise pass in a courseId arg
class CourseRoundsFragment : RoundsFragment() {

    override fun getRoundRestrictions(): RoundRestrictions {
        val courseId = arguments!!.getInt("courseId")
        return RoundRestrictions().withCurrentUser().withCourse(courseId)
    }

}