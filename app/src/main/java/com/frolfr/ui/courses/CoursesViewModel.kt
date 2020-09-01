package com.frolfr.ui.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.domain.model.Course
import com.frolfr.domain.model.UserScorecard
import com.frolfr.domain.repository.CourseRepository
import com.frolfr.domain.repository.ScorecardRepository
import kotlinx.coroutines.*
import java.util.*

class CoursesViewModel : ViewModel() {

    val courses: LiveData<List<Course>> = loadCourses()
    private val _coursesFetched = MutableLiveData<Boolean>()
    private val _additionalCoursesFetched = MutableLiveData<Boolean>()

    // TODO get this into the UI
    val lastPlayedByCourse = MutableLiveData<Map<Int, Date>>()

    private val _navigateToCourseDetail = MutableLiveData<Course>()
    val navigateToCourseDetail: LiveData<Course>
        get() = _navigateToCourseDetail

    private val _refreshComplete = MutableLiveData<Boolean>()
    val refreshComplete: LiveData<Boolean>
        get() = _refreshComplete

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    init {
        loadLastPlayedByCourse()
    }

    fun onCourseClicked(course: Course) {
        _navigateToCourseDetail.value = course
    }

    fun onCourseNavigated() {
        _navigateToCourseDetail.value = null
    }

    private fun loadCourses(): LiveData<List<Course>> {
        return CourseRepository().getCourses()
    }

    fun fetchCourses() {
        _coursesFetched.value = true
        coroutineScope.launch {
            CourseRepository().fetchAllCourses()
        }
    }

    fun fetchAdditionalCourses() {
        _additionalCoursesFetched.value = true
        coroutineScope.launch {
            CourseRepository().fetchMissingCourses()
        }
    }

    fun fetchedCourses(): Boolean {
        return _coursesFetched.value ?: false
    }

    fun fetchedAdditionalCourses(): Boolean {
        return _additionalCoursesFetched.value ?: false
    }

    fun refreshCourses() {
        GlobalScope.launch(Dispatchers.Main) {
            coroutineScope.launch {
                CourseRepository().fetchMissingCourses()
            }.join()
            _refreshComplete.value = true
        }
    }

    fun onRefreshCompleteAcknowledged() {
        _refreshComplete.value = false
    }

    private fun loadLastPlayedByCourse() {
        GlobalScope.launch(Dispatchers.Main) {
            var lastPlayedByCourseResult: Map<Int, Date>? = null
            coroutineScope.launch {
                lastPlayedByCourseResult = ScorecardRepository().getLastPlayedByCourse()
            }.join()
            lastPlayedByCourse.value = lastPlayedByCourseResult
        }
    }

}