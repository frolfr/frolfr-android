package com.frolfr.ui.scorecard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.FrolfrApi
import com.frolfr.api.model.ScorecardResponse
import com.frolfr.api.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.min

class ScorecardViewModel(private val scorecardId: Int) : ViewModel() {

    private val _scorecard = MutableLiveData<Scorecard>()
    val scorecard: LiveData<Scorecard>
        get() = _scorecard

    private val sectionVisibility = mutableMapOf<Int, Boolean>()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        loadScorecard()
    }

    private fun loadScorecard() {
        coroutineScope.launch {
            try {
                val scorecardResponse = FrolfrApi.retrofitService.scorecard(scorecardId)
                val scorecardResponseAdapter = ScorecardResponseAdapter(scorecardResponse)
                _scorecard.value = scorecardResponseAdapter.toScorecard()
            } catch (t: Throwable) {
                Log.i("frolfrScorecard/Round", "Got error result", t)
            }
        }
    }

    fun getHoleNumberStr(sectionNum: Int, holeIndex: Int): String {
        val holeNumber = holeNumFromSectionAndHoleIndex(sectionNum, holeIndex)
        if (holeNumber <= scorecard.value!!.holeMeta.size) {
            return holeNumber.toString()
        }
        return ""
    }

    fun getParStr(sectionNum: Int, holeIndex: Int): String {
        val holeNumber = holeNumFromSectionAndHoleIndex(sectionNum, holeIndex)
        if (holeNumber <= scorecard.value!!.holeMeta.size) {
            return (scorecard.value!!.holeMeta[holeNumber] ?: error("")).par.toString()
        }
        return ""
    }

    fun getUserHoleScore(sectionNum: Int, holeIndex: Int, userId: Int): String {
        val holeNumber = holeNumFromSectionAndHoleIndex(sectionNum, holeIndex)
        if (holeNumber <= scorecard.value!!.holeMeta.size) {
            val strokes = (scorecard.value!!.userHoleResults[Pair(userId, holeNumber)] ?: error("")).strokes
            if (strokes != null) {
                return strokes.toString()
            }
        }
        return ""
    }

    private fun holeNumFromSectionAndHoleIndex(sectionNum: Int, holeIndex: Int): Int {
        return ((sectionNum - 1) * 9) + holeIndex
    }

    fun getUserInitials(userId: Int): String {
        val user = scorecard.value!!.users[userId]
        val firstInitial = user!!.nameFirst[0].toUpperCase().toString()
        return if (user.nameLast != null) {
            firstInitial + user.nameLast[0].toUpperCase()
        } else {
            firstInitial
        }
    }

    fun getSectionHeader(sectionNum: Int): String {
        val numHoles = scorecard.value!!.holeMeta.size
        val holesInSection = min(9, numHoles - (9 * (sectionNum - 1)))
        val sectionPrefix = when (sectionNum) {
            1 -> "Front"
            2 -> "Back"
            else -> "Extra"
        }
        return "$sectionPrefix $holesInSection"
    }

    fun isSectionVisible(sectionIndex: Int): Boolean {
        return sectionVisibility.getOrDefault(sectionIndex, true)
    }

    fun toggleSectionVisibility(sectionIndex: Int): Unit {
        val visibility = isSectionVisible(sectionIndex)
        sectionVisibility[sectionIndex] = !visibility
    }

    override fun onCleared() {
        super.onCleared()
    }
}

// TODO move elsewhere
class ScorecardResponseAdapter(private val scorecardResponse: ScorecardResponse) {
    suspend fun toScorecard(): Scorecard {
        val roundId = scorecardResponse.roundInfo.id
        val userScorecards = scorecardResponse.userScorecards
        val userIdByScorecardId = userScorecards
            .associateBy { userScorecard -> userScorecard.id }
            .mapValues { userScorecard -> userScorecard.value.userId }
        val userIds = userScorecards.map { userScorecard -> userScorecard.userId }
        val users = userIds
            .map { userId -> FrolfrApi.retrofitService.user(userId).user }
            .associateBy { user -> user.id }
        val holeMeta = mutableMapOf<Int, HoleMeta>()
        val userHoleResults = mutableMapOf<Pair<Int, Int>, HoleResult>()
        scorecardResponse.holeResults.map { holeResult ->
            holeMeta.putIfAbsent(holeResult.hole, HoleMeta(holeResult.par))
            userHoleResults.put(
                Pair(userIdByScorecardId[holeResult.userScorecardId] ?: error(""), holeResult.hole),
                HoleResult(holeResult.strokes))
        }
        return Scorecard(roundId, holeMeta, users, userHoleResults)
    }
}

// TODO move elsewhere
data class Scorecard(
    val roundId: Int,
    val holeMeta: Map<Int, HoleMeta>,
    val users: Map<Int, User>,
    val userHoleResults: Map<Pair<Int, Int>, HoleResult>
)
data class HoleMeta(val par: Int)
data class HoleResult(val strokes: Int?)