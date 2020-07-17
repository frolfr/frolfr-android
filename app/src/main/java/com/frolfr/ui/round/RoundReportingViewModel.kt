package com.frolfr.ui.round

import android.util.Log
import androidx.databinding.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.FrolfrApi
import com.frolfr.api.model.Round
import com.frolfr.api.model.Turn2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RoundReportingViewModel(private val roundId: Int) : ViewModel() {

    private val _round = MutableLiveData<Round>()
    val round: LiveData<Round>
        get() = _round

    private val _parMap = MutableLiveData<Map<Int, Int>>()
    val parMap: LiveData<Map<Int, Int>>
        get() = _parMap

    private val _currentPar = MutableLiveData<Int>()
    val currentPar: LiveData<Int>
        get() = _currentPar

    private val _currentHole = MutableLiveData<Int>()
    val currentHole: LiveData<Int>
        get() = _currentHole

    private val _userStrokes = ObservableArrayMap<Pair<Int, Int>, Int>()
    val userStrokes: ObservableMap<Pair<Int, Int>, Int>
        get() = _userStrokes

    val currentUserStrokes = ObservableArrayMap<Int, Int>()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _navigateToRoundScorecard = MutableLiveData<Int>()
    val navigateToRoundScorecard: LiveData<Int>
        get() = _navigateToRoundScorecard

    private val scorecardToUserMap: MutableMap<Int, Int> = mutableMapOf()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _currentHole.value = 1
        _error.value = null
        loadRound()
    }

    private fun loadRound() {
        coroutineScope.launch {
            try {
                Log.i("loadRound", "Started for round $roundId")

                val round = FrolfrApi.retrofitService.round(roundId)

                _parMap.value = mutableMapOf()
                round.getScorecards()[0].getTurns().forEachIndexed { index, turn ->
                    (_parMap.value as MutableMap<Int, Int>)[index + 1] = turn.par
                }

                round.getScorecards().forEach {
                    val scorecardUser = FrolfrApi.retrofitService.scorecardUser(it.id.toInt())
                    scorecardToUserMap[it.id.toInt()] = scorecardUser.id.toInt()
                }

                _round.value = round

                onHoleChanged()

                Log.i("loadRound", "Loaded Round: $round")
            } catch (t: Throwable) {
                Log.i("loadRound", "Got error result", t)
            }
        }
    }

    private fun getStrokesForUser(userId: Int): Int {
        val strokes = userStrokes[Pair(userId, currentHole.value!!)]
        if (strokes == null || strokes == 0) {
            return 0
        }
        return strokes
    }

    fun getStrokesStrForUser(userId: Int): String {
        val strokes = getStrokesForUser(userId)
        if (strokes == 0) {
            return "-"
        }
        return strokes.toString()
    }

    fun getPar(holeIndex: Int = currentHole.value!!): Int {
        return if (parMap.value != null) parMap.value!!.getOrDefault(holeIndex, 3) else 3
    }

    private fun onHoleChanged() {
        _currentPar.value = getPar()

        for (user in round.value!!.getUsers()) {
            currentUserStrokes[user.id.toInt()] = userStrokes[Pair(user.id.toInt(), currentHole.value!!)] ?: 0
        }
    }

    fun onStrokesMinusClicked(userId: Int) {
        val strokes = userStrokes[Pair(userId, currentHole.value!!)]
        if (strokes == null || strokes == 0) {
            _userStrokes[Pair(userId, currentHole.value!!)] = getPar() - 1
        } else {
            _userStrokes[Pair(userId, currentHole.value!!)] = strokes - 1
        }
        currentUserStrokes[userId] = userStrokes[Pair(userId, currentHole.value!!)]
    }

    fun onStrokesPlusClicked(userId: Int) {
        val strokes = userStrokes[Pair(userId, _currentHole.value!!)]
        if (strokes == null || strokes == 0) {
            _userStrokes[Pair(userId, currentHole.value!!)] = getPar()
        } else {
            _userStrokes[Pair(userId, currentHole.value!!)] = strokes + 1
        }
        currentUserStrokes[userId] = userStrokes[Pair(userId, currentHole.value!!)]
    }

    fun onPreviousHoleClicked() {
        if (currentHole.value != 1) {
            _currentHole.value = currentHole.value?.minus(1)
        }
        onHoleChanged()
    }

    fun onSubmitHoleClicked() {
        val turns: MutableList<Turn2> = mutableListOf()

        for (user in round.value!!.getUsers()) {
            val userStrokes = getStrokesForUser(user.id.toInt())
            if (userStrokes == null || userStrokes < 1) {
                _error.value = "Must finish reporting strokes!"
                return
            }

            val scorecard = round.value!!.getScorecards().find { scorecard ->
                scorecardToUserMap[scorecard.id.toInt()] == user.id.toInt()
            }
            val existingTurn = scorecard!!.getTurns()[currentHole.value!!.minus(1)]

            val turn = Turn2()
            turn.id = existingTurn.id
            turn.holeNumber = currentHole.value!!
            turn.par = getPar()
            turn.strokes = userStrokes

            turns.add(turn)
        }

        coroutineScope.launch {
            try {
                turns.forEach { turn ->
                    FrolfrApi.retrofitService.reportTurn(turn.id.toInt(), turn)
                }

                val turnIds = turns.map { turn ->
                    turn.id
                }.reduce { acc, string -> "${acc}\n${string}"}

                Log.i("reportTurn", "All turns reported: $turnIds")

                if (currentHole.value == round.value!!.getCourse().holeCount) {
                    _navigateToRoundScorecard.value = round.value!!.id.toInt()
                } else {
                    _currentHole.value = currentHole.value?.plus(1)
                    onHoleChanged()
                }
            } catch (t: Throwable) {
                Log.i("reportTurn", "Got error result", t)
            }
        }
    }

    fun clearErrors() {
        _error.value = null
    }

    fun onRoundScorecardNavigated() {
        _navigateToRoundScorecard.value = null
    }

}