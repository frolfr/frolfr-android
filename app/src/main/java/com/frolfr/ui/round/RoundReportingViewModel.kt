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
import java.lang.Math.min

class RoundReportingViewModel(private val roundId: Int) : ViewModel() {

    private val _round = MutableLiveData<Round>()
    val round: LiveData<Round>
        get() = _round

    private val _parMap = MutableLiveData<MutableMap<Int, Int>>()
    val parMap: LiveData<MutableMap<Int, Int>>
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

    // TODO Do I need to unsubscribe the UI from observing this guy? Is that even possible?
    //      Would it be better to go back to the RecyclerView and try out 2-way data binding?
    val currentUserStrokes = ObservableArrayMap<Int, Int>()
    val currentUserScores = ObservableArrayMap<Int, Int>()

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
        _error.value = null
        loadRound()
    }

    private fun loadRound() {
        coroutineScope.launch {
            try {
                Log.i("loadRound", "Started for round $roundId")

                val round = FrolfrApi.retrofitService.round(roundId)

                _parMap.value = mutableMapOf()
                round.getScorecards()[0].getTurns().forEach { turn ->
                    (_parMap.value as MutableMap<Int, Int>)[turn.holeNumber] = turn.par
                }

                round.getUsers().forEach { user ->
                    currentUserScores[user.id.toInt()] = 0;
                }

                var minUnreportedHole = round.getCourse().holeCount + 1
                round.getScorecards().forEach { scorecard ->
                    val scorecardUser = FrolfrApi.retrofitService.scorecardUser(scorecard.id.toInt())
                    scorecardToUserMap[scorecard.id.toInt()] = scorecardUser.id.toInt()

                    scorecard.getTurns().forEach { turn ->
                        if (turn.strokes != null) {
                            userStrokes[Pair(scorecardUser.id.toInt(), turn.holeNumber)] =
                                turn.strokes
                            currentUserScores[scorecardUser.id.toInt()] =
                                currentUserScores[scorecardUser.id.toInt()]?.plus(turn.getScore())
                        } else {
                            minUnreportedHole = minUnreportedHole.coerceAtMost(turn.holeNumber)
                        }
                    }
                }

                _round.value = round

                _currentHole.value = if (minUnreportedHole > round.getCourse().holeCount) 1 else minUnreportedHole
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

        val userScoreChanges = mutableMapOf<Int, Int>()

        for (user in round.value!!.getUsers()) {
            val userStrokes = getStrokesForUser(user.id.toInt())
            if (userStrokes == null || userStrokes < 1) {
                _error.value = "Must finish reporting strokes!"
                return
            }

            val scorecard = round.value!!.getScorecards().find { scorecard ->
                scorecardToUserMap[scorecard.id.toInt()] == user.id.toInt()
            }
            val currentTurn = scorecard!!.getTurns().find { turn ->
                turn.holeNumber == currentHole.value!!
            }

            val turn = Turn2()
            turn.id = currentTurn!!.id
            turn.holeNumber = currentTurn.holeNumber
            turn.par = currentPar.value!!
            turn.strokes = userStrokes

            if (currentTurn.par != turn.par || currentTurn.strokes != turn.strokes) {
                turns.add(turn)

                userScoreChanges[user.id.toInt()] = userScoreChanges.getOrDefault(user.id.toInt(), 0)
                    .minus(currentTurn.getScore())
                    .plus(turn.getScore())
            }
        }

        coroutineScope.launch {
            try {
                turns.forEach { turn ->
                    FrolfrApi.retrofitService.reportTurn(turn.id.toInt(), turn)
                }

                for (user in round.value!!.getUsers()) {
                    currentUserScores[user.id.toInt()] = currentUserScores[user.id.toInt()]
                        ?.plus(userScoreChanges[user.id.toInt()]!!)
                }

                val turnIds = turns.map { turn ->
                    turn.id
                }.fold("") { acc, string -> "${acc}\n${string}"}

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

    fun setHole(holeNumber: Int) {
        _currentHole.value = holeNumber
        onHoleChanged()
    }

    fun setPar(parNumber: Int) {
        _parMap.value?.put(currentHole.value!!, parNumber)
        _currentPar.value = parNumber
    }

}