package com.frolfr.ui.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.frolfr.api.model.Course

class CourseViewModelFactory(private val course: Course) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            return CourseViewModel(
                theCourse = course
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
