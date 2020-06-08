package com.frolfr.ui.courses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.model.Course

class CoursesViewModel : ViewModel() {

    val courses = MutableLiveData<List<Course>>().apply {
        value = emptyList()
    }

    override fun onCleared() {
        super.onCleared()
    }
}