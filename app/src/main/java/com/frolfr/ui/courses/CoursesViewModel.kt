package com.frolfr.ui.courses

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.FrolfrApi
import com.frolfr.api.model.Course
import com.frolfr.api.model.Course2
import com.frolfr.api.model.Round
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Integer.max

class CoursesViewModel : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 10
    }
    private var fetchedPages = 1
    private var totalPages = -1

    val courses = MutableLiveData<List<Course>>().apply {
        value = emptyList()
    }

    private val _navigateToCourseDetail = MutableLiveData<Course>()
    val navigateToCourseDetail
        get() = _navigateToCourseDetail

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        loadCoursePage(1)
    }

    fun onCourseClicked(course: Course) {
        _navigateToCourseDetail.value = course
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
//            try {
//                // TODO constrain to just your rounds, sort
//                val roundsResponse = FrolfrApi.retrofitService.rounds(pageNum, PAGE_SIZE, "course,users")
//                Log.i("roundsResponse", roundsResponse.toString())
//                val roundsDocument = roundsResponse.asArrayDocument<Round>()
//                val course2 = roundsDocument[0].course.get(roundsResponse)
//                val fetchedCourses = listOf(
//                    Course(
//                        course2.id.toInt(),
//                        course2.city,
//                        course2.state,
//                        course2.country,
//                        course2.name,
//                        "",
//                        "Atlanta, GA",
//                        emptyList(),
//                        "2020-05-19T01:45:05.333Z",
//                        emptyList(),
//                        emptyList(),
//                        course2.holeCount
//                    )
//                )
//                courses.value = courses.value?.plus(fetchedCourses)
//                totalPages = 1 // userCoursesResponse.meta.totalPages
//                fetchedPages = 1 // max(fetchedPages, pageNum)
//            } catch (t: Throwable) {
//                Log.i("frolfrUserCourses", "Got error result", t)
//            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}