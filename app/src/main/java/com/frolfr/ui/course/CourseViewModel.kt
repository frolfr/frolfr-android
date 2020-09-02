package com.frolfr.ui.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.frolfr.domain.model.Course
import com.frolfr.domain.repository.CourseRepository
import kotlinx.coroutines.*

class CourseViewModel(val courseId: Int) : ViewModel() {

    val course: LiveData<Course> = loadCourse()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    private fun loadCourse(): LiveData<Course> {
        return CourseRepository().getCourse(courseId)
    }

}