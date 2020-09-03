package com.frolfr.ui.course.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CourseOverviewViewModelFactory(private val courseId: Int) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseOverviewViewModel::class.java)) {
            return CourseOverviewViewModel(
                courseId = courseId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
