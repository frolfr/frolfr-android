package com.frolfr.ui.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CourseViewModelFactory(private val courseId: Int) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            return CourseViewModel(
                courseId = courseId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
