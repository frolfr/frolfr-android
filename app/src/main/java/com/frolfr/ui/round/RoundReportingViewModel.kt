package com.frolfr.ui.round

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
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


class RoundReportingViewModel(private val roundId: Int) : ViewModel(), Observable {

    private val _round = MutableLiveData<Round>()
    val round: LiveData<Round>
        get() = _round

    private val _parMap = MutableLiveData<Map<Int, Int>>()
    val parMap: LiveData<Map<Int, Int>>
        get() = _parMap

    private val _currentHole = MutableLiveData<Int>()
    val currentHole: LiveData<Int>
        get() = _currentHole

    private val _userStrokes = MutableLiveData<MutableMap<Pair<Int, Int>, Int>>()
    val userStrokes: LiveData<MutableMap<Pair<Int, Int>, Int>>
        @Bindable get() = _userStrokes

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
        _userStrokes.value = mutableMapOf()
        _error.value = null
        loadRound()
    }

    private fun loadRound() {
        coroutineScope.launch {
            try {
                Log.i("loadRound", "Started for round $roundId")

                val round = FrolfrApi.retrofitService.round(roundId)
                _round.value = round

                _parMap.value = mutableMapOf()
                round.getScorecards()[0].getTurns().forEachIndexed { index, turn ->
                    (_parMap.value as MutableMap<Int, Int>)[index + 1] = turn.par
                }

                round.getScorecards().forEach {
                    val scorecardUser = FrolfrApi.retrofitService.scorecardUser(it.id.toInt())
                    scorecardToUserMap[it.id.toInt()] = scorecardUser.id.toInt()
                }

                Log.i("loadRound", "Loaded Round: $round")
            } catch (t: Throwable) {
                Log.i("loadRound", "Got error result", t)
            }
        }
    }

    fun getStrokesForUser(userId: Int): Int {
        val strokes = userStrokes.value!![Pair(userId, currentHole.value!!)]
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

    fun onStrokesMinusClicked(userId: Int) {
        val strokes = userStrokes.value!![Pair(userId, currentHole.value!!)]
        if (strokes == null || strokes == 0) {
            _userStrokes.value!![Pair(userId, currentHole.value!!)] = getPar() - 1
        } else {
            _userStrokes.value!![Pair(userId, currentHole.value!!)] = strokes - 1
        }
    }

    fun onStrokesPlusClicked(userId: Int) {
        val strokes = userStrokes.value!![Pair(userId, _currentHole.value!!)]
        if (strokes == null || strokes == 0) {
            _userStrokes.value!![Pair(userId, currentHole.value!!)] = getPar()
        } else {
            _userStrokes.value!![Pair(userId, currentHole.value!!)] = strokes + 1
        }
    }

    fun onPreviousHoleClicked() {
        if (currentHole.value != 1) {
            _currentHole.value = currentHole.value?.minus(1)
        }
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


    // Begin Observable Code

    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback( callback: Observable.OnPropertyChangedCallback) {
        callbacks.remove(callback)
    }

    /**
     * Notifies observers that all properties of this instance have changed.
     */
    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies observers that a specific property has changed. The getter for the
     * property that changes should be marked with the @Bindable annotation to
     * generate a field in the BR class to be used as the fieldId parameter.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }

}