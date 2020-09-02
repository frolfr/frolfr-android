package com.frolfr.ui.rounds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.domain.model.Round
import com.frolfr.domain.repository.RoundRepository
import kotlinx.coroutines.*

class RoundsViewModel(val roundRestrictions: RoundRestrictions) : ViewModel() {

    val rounds: LiveData<List<Round>> = loadRounds()

    private val _navigateToRoundDetail = MutableLiveData<Round>()
    val navigateToRoundDetail: LiveData<Round>
        get() = _navigateToRoundDetail

    private val _refreshComplete = MutableLiveData<Boolean>()
    val refreshComplete: LiveData<Boolean>
        get() = _refreshComplete

    private val _roundsFetched = MutableLiveData<Boolean>()
    private val _additionalRoundsFetched = MutableLiveData<Boolean>()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)


    fun onRoundClicked(round: Round) {
        _navigateToRoundDetail.value = round
    }

    fun onRoundNavigated() {
        _navigateToRoundDetail.value = null
    }

    private fun loadRounds(): LiveData<List<Round>> {
        roundRestrictions.courseId?.let {
            return RoundRepository().getCourseRounds(it)
        }
        return RoundRepository().getRounds()
    }

    fun fetchRounds() {
        _roundsFetched.value = true
        coroutineScope.launch {
            RoundRepository().fetchAllRounds()
        }
    }

    fun fetchAdditionalRounds() {
        _additionalRoundsFetched.value = true
        coroutineScope.launch {
            RoundRepository().fetchMissingRounds()
        }
    }

    fun fetchedRounds(): Boolean {
        return _roundsFetched.value ?: false
    }

    fun fetchedAdditionalRounds(): Boolean {
        return _additionalRoundsFetched.value ?: false
    }

    fun refreshRounds() {
        // TODO could store an updatedAt field on Round (maybe other entities)
        //      and use that to know when the db (cache) should be invalidated

        // TODO for now, this should probably just load the first page of rounds
        //      and ensure scores are updated when saving
        GlobalScope.launch(Dispatchers.Main) {
            coroutineScope.launch {
                RoundRepository().fetchRoundsPage(1)
            }.join()
            _refreshComplete.value = true
        }
    }

    fun onRefreshCompleteAcknowledged() {
        _refreshComplete.value = false
    }

}