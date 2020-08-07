package com.frolfr.ui.scorecard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.FrolfrApi
import com.frolfr.api.model.Round
import com.frolfr.api.model.ScorecardResponse
import com.frolfr.api.model.User
import com.frolfr.api.model.User2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.min

class ScorecardViewModel(private val roundId: Int) : ViewModel() {

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
                val round = FrolfrApi.retrofitService.round(roundId)
                val scorecardResponseAdapter = ScorecardResponseAdapter(round)
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
            return scorecard.value!!.holeMeta[holeNumber]?.par.toString()
        }
        return ""
    }

    fun getUserHoleScore(sectionNum: Int, holeIndex: Int, userId: Int): String {
        val holeNumber = holeNumFromSectionAndHoleIndex(sectionNum, holeIndex)
        val strokes = scorecard.value!!.userHoleResults[Pair(userId, holeNumber)]?.strokes
        if (strokes != null) {
            return strokes.toString()
        }
        return ""
    }

    fun getUserSectionScore(sectionNum: Int, userId: Int): Int {
        val holeNumberMin = holeNumFromSectionAndHoleIndex(sectionNum, 1)
        val holeNumberMax = min(holeNumFromSectionAndHoleIndex(sectionNum, 9), scorecard.value!!.holeMeta.size)
        var score = 0
        for (i in holeNumberMin..holeNumberMax) {
            val par: Int = scorecard.value!!.holeMeta[i]!!.par
            val strokes: Int? = scorecard.value!!.userHoleResults[Pair(userId, i)]?.strokes
            if (strokes != null) {
                score += (strokes - par)
            }
        }
        return score
    }

    private fun holeNumFromSectionAndHoleIndex(sectionNum: Int, holeIndex: Int): Int {
        return ((sectionNum - 1) * 9) + holeIndex
    }

    fun getUserInitials(userId: Int): String {
        val user = scorecard.value!!.users[userId]
        val firstInitial = user!!.firstName[0].toUpperCase().toString()
        return if (user.lastName != null) {
            firstInitial + user.lastName!![0].toUpperCase()
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

    fun toggleSectionVisibility(sectionIndex: Int) {
        val visibility = isSectionVisible(sectionIndex)
        sectionVisibility[sectionIndex] = !visibility
    }

    fun getUserScore(userId: Int): Int {
        var score = 0
        for (i in 1..scorecard.value!!.holeMeta.size) {
            val par: Int = scorecard.value!!.holeMeta[i]!!.par
            val strokes: Int? = scorecard.value!!.userHoleResults[Pair(userId, i)]?.strokes
            if (strokes != null) {
                score += (strokes - par)
            }
        }
        return score
    }

    fun getUserStrokes(userId: Int): Int {
        var strokes = 0
        for (i in 1..scorecard.value!!.holeMeta.size) {
            val holeStrokes: Int? = scorecard.value!!.userHoleResults[Pair(userId, i)]?.strokes
            if (holeStrokes != null) {
                strokes += holeStrokes
            }
        }
        return strokes
    }
}

// TODO move elsewhere
class ScorecardResponseAdapter(private val round: Round) {
    fun toScorecard(): Scorecard {
        val roundId = round.id.toInt()
        val userScorecards = round.getScorecards()
        val userIdByScorecardId = userScorecards
            .associateBy { userScorecard -> userScorecard.id.toInt() }
            .mapValues { userScorecard -> userScorecard.value.getUser().id.toInt() }
        val users = round.getUsers()
            .associateBy { user -> user.id.toInt() }
        val holeMeta = mutableMapOf<Int, HoleMeta>()
        val userHoleResults = mutableMapOf<Pair<Int, Int>, HoleResult>()
        round.getScorecards().forEach { scorecard ->
            scorecard.getTurns().forEach { turn ->
                holeMeta.putIfAbsent(turn.holeNumber, HoleMeta(turn.par))
                userHoleResults[
                    Pair(userIdByScorecardId[scorecard.id.toInt()]!!, turn.holeNumber)
                ] = HoleResult(turn.strokes)
            }
        }
        return Scorecard(roundId, holeMeta, users, userHoleResults)
    }
}

// TODO move elsewhere
data class Scorecard(
    val roundId: Int,
    val holeMeta: Map<Int, HoleMeta>,
    val users: Map<Int, User2>,
    val userHoleResults: Map<Pair<Int, Int>, HoleResult>
)
data class HoleMeta(val par: Int)
data class HoleResult(val strokes: Int?)