package com.frolfr.ui.course.tab.userCourseScorecards

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.FrolfrApi
import com.frolfr.api.model.UserScorecardSummary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserCourseScorecardsViewModel(private val courseId: Int) : ViewModel() {

    val userScorecards = MutableLiveData<List<UserScorecardSummary>>().apply {
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
                val userScorecardsResponse = FrolfrApi.retrofitService.userCourseScorecards(courseId)
                val scorecardsDesc = userScorecardsResponse.userScorecardSummaries.sortedByDescending {
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
