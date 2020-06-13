package com.frolfr.ui.course

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.model.Course

class CourseViewModel(private val theCourse: Course) : ViewModel() {

    val course = MutableLiveData<Course>().apply {
        value = theCourse
    }

    override fun onCleared() {
        super.onCleared()
    }
}