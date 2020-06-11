package com.frolfr.ui.courseDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.model.Course

class CourseDetailViewModel : ViewModel() {

    val course = MutableLiveData<Course>().apply {
        value = null
    }
}
