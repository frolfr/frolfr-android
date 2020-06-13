package com.frolfr.ui.course.tab.userCourseScorecards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserCourseScorecardsViewModelFactory(private val courseId: Int) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserCourseScorecardsViewModel::class.java)) {
            return UserCourseScorecardsViewModel(
                courseId = courseId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
