package com.frolfr.ui.course.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.frolfr.domain.model.Course
import com.frolfr.domain.model.ScorecardStats
import com.frolfr.domain.repository.CourseRepository
import kotlinx.coroutines.*

class CourseOverviewViewModel(val courseId: Int) : ViewModel() {

    // TODO
    //       Scorecards played (completed/not)
    //       Average Score/Strokes
    //       Best Score/Strokes
    //       Shot Breakdown (Eagle, Birdie, Par, Bogie, Other) % and #

    val course: LiveData<Course> = loadCourse()
    val stats: LiveData<ScorecardStats> = loadScorecardStats()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    private fun loadCourse(): LiveData<Course> {
        return CourseRepository().getCourse(courseId)
    }

    private fun loadScorecardStats(): LiveData<ScorecardStats> {
        return CourseRepository().getScorecardStats(courseId)
    }

}