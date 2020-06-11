package com.frolfr.ui.courses

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.FrolfrApi
import com.frolfr.api.model.Course
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Integer.max

class CoursesViewModel : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 5 // TODO The API doesn't respect this query param
    }
    private var fetchedPages = 1
    private var totalPages = -1

    val courses = MutableLiveData<List<Course>>().apply {
        value = emptyList()
    }

    private val _navigateToCourseDetail = MutableLiveData<Int>()
    val navigateToCourseDetail
        get() = _navigateToCourseDetail

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        loadCoursePage(1)
    }

    fun onCourseClicked(courseId: Int) {
        _navigateToCourseDetail.value = courseId
    }

    fun onCourseNavigated() {
        _navigateToCourseDetail.value = null
    }


    // TODO why can't I do an object here that has scope to these variables?
//    object listEndListener : ListEndListener() {
//        override fun onListEnd() {
//            if (this.fetchedPages < totalPages) {
//
//            }
//        }
//    }

    inner class PageOnListEndListener : ListEndListener() {
        override fun onListEnd() {
            if (fetchedPages < totalPages) {
                loadCoursePage(fetchedPages + 1)
            }
        }
    }

    fun loadCoursePage(pageNum: Int) {
        coroutineScope.launch {
            try {
                val userCoursesResponse = FrolfrApi.retrofitService.userCourses(pageNum, PAGE_SIZE)
                courses.value = courses.value?.plus(userCoursesResponse.courses)
                totalPages = userCoursesResponse.meta.totalPages
                fetchedPages = max(fetchedPages, pageNum)
            } catch (t: Throwable) {
                Log.i("frolfrUserCourses", "Got error result", t)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}