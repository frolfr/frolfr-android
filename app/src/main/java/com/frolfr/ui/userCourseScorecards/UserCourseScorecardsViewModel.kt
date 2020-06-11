package com.frolfr.ui.userCourseScorecards

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.FrolfrApi
import com.frolfr.api.model.Course
import com.frolfr.api.model.UserScorecard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserCourseScorecardsViewModel : ViewModel() {

    val course = MutableLiveData<Course>().apply {
        value = null
    }

    val userScorecards = MutableLiveData<List<UserScorecard>>().apply {
        value = emptyList()
    }

    private val _navigateToRoundDetail = MutableLiveData<Int>()
    val navigateToRoundDetail
        get() = _navigateToRoundDetail

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        loadUserScorecards()
    }

    fun onUserScorecardClicked(roundId: Int) {
        _navigateToRoundDetail.value = roundId
    }

    fun onRoundNavigated() {
        _navigateToRoundDetail.value = null
    }

    private fun loadUserScorecards() {
        coroutineScope.launch {
            try {
                val userScorecardsResponse = FrolfrApi.retrofitService.userCourseScorecards(course.value!!.id)
                val scorecardsDesc = userScorecardsResponse.userScorecards.sortedByDescending {
                    it.date
                }
                userScorecards.value = scorecardsDesc
            } catch (t: Throwable) {
                Log.i("frolfrUserScorecards", "Got error result", t)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
