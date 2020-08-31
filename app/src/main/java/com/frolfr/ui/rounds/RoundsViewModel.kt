package com.frolfr.ui.rounds

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.FrolfrApi
import com.frolfr.api.FrolfrAuthorization
import com.frolfr.api.PaginationLinksAdapter
import com.frolfr.api.model.PaginationLinks
import com.frolfr.domain.model.Round
import com.frolfr.config.PaginationConfig
import com.frolfr.domain.repository.RoundRepository
import kotlinx.coroutines.*
import moe.banana.jsonapi2.ArrayDocument

class RoundsViewModel : ViewModel() {

    companion object {
        private const val PAGE_SIZE = PaginationConfig.DEFAULT_PAGE_SIZE
    }
    private var fetchedPages = 1
    private var hasNextPage = true

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

    init {
        // TODO
//        loadRoundsPage(1)
//        loadRounds()
    }

    fun onRoundClicked(round: Round) {
        _navigateToRoundDetail.value = round
    }

    fun onRoundNavigated() {
        _navigateToRoundDetail.value = null
    }

    // TODO Should really call this _before_ the list end...
    //      There are probably paging adapter to be used here
    //      once I add a SQL cache to network requests...
    inner class PageOnListEndListener : ListEndListener() {
        override fun onListEnd() {
            if (hasNextPage) {
                loadRoundsPage(++fetchedPages)
            }
        }
    }

    private fun loadRounds(): LiveData<List<Round>> {
//        coroutineScope.launch {
//            val roundsResponse = RoundRepository().getRounds()
//            rounds.value = roundsResponse.value
//        }

        val roundsResponse = RoundRepository().getRounds()
        return roundsResponse
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
            RoundRepository().fetchRoundsSince(rounds.value!![0]!!.createdAt!!)
            // TODO Should store in the db whether or not we've fetched all historical
            //      rounds. If not, we need to call a "fetchRoundsBefore"
        }
    }

    fun fetchedRounds(): Boolean {
        return _roundsFetched?.value ?: false
    }

    fun fetchedAdditionalRounds(): Boolean {
        return _additionalRoundsFetched?.value ?: false
    }

    fun loadRoundsPage(pageNum: Int) {
        // TODO
//        coroutineScope.launch {
//            val roundsDocument = loadRoundsPageCoroutine(pageNum)
//            rounds.value = rounds.value?.plus(roundsDocument)
//            val paginationLinks = roundsDocument.links.get<PaginationLinks>(PaginationLinksAdapter()) as PaginationLinks
//            hasNextPage = paginationLinks.hasNextPage()
//        }
    }

//    private suspend fun loadRoundsPageCoroutine(pageNum: Int): ArrayDocument<Round> {
//        try {
//            val userId = FrolfrAuthorization.userId
//            return FrolfrApi.retrofitService.rounds(pageNum, PAGE_SIZE, userId)
//        } catch (t: Throwable) {
//            Log.i("frolfrRounds", "Got error result", t)
//            throw t
//        }
//    }

    fun refreshRounds() {
//        coroutineScope.launch {
//            val roundsDocument = loadRoundsPageCoroutine(1)
//            val otherExistingRounds = rounds.value?.filter { round ->
//                !roundsDocument.contains(round)
//            }
//            if (otherExistingRounds != null) {
//                rounds.value = roundsDocument.plus(otherExistingRounds)
//            } else {
//                rounds.value = roundsDocument
//            }
//            _refreshComplete.value = true
//        }

        // TODO could store an updatedAt field on Round (maybe other entities)
        //      and use that to know when the db (cache) should be invalidated

        // TODO for now, this should probably just load the first page of rounds
        //      and ensure scores are updated when saving
        GlobalScope.launch(Dispatchers.Main) {
            coroutineScope.launch {
                RoundRepository().fetchRoundsPage(1)
                // TODO Should store in the db whether or not we've fetched all historical
                //      rounds. If not, we need to call a "fetchRoundsBefore"
            }.join()
            _refreshComplete.value = true
        }
    }

    fun onRefreshCompleteAcknowledged() {
        _refreshComplete.value = false
    }

}