package com.frolfr.ui.round

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.FrolfrApi
import com.frolfr.api.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RoundReportingViewModel(private val roundId: Int) : ViewModel() {

    private val _round = MutableLiveData<Round>()
    val round: LiveData<Round>
        get() = _round

    private val _currentHole = MutableLiveData<Int>()
    val currentHole: LiveData<Int>
        get() = _currentHole

    private val _userStrokes = MutableLiveData<Map<User2, Int>>()
    val userStrokes: LiveData<Map<User2, Int>>
        get() = _userStrokes

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _currentHole.value = 1
        _userStrokes.value = mutableMapOf()
        loadRound()
    }

    private fun loadRound() {
        coroutineScope.launch {
            try {
                val round = FrolfrApi.retrofitService.round(roundId, "course,users,scorecards")
                _round.value = round
                Log.i("loadRound", "Loaded Round: $round")
            } catch (t: Throwable) {
                Log.i("loadRound", "Got error result", t)
            }
        }
    }
}