package com.frolfr.ui.rounds

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.FrolfrApi
import com.frolfr.api.FrolfrAuthorization
import com.frolfr.api.PaginationLinksAdapter
import com.frolfr.api.model.PaginationLinks
import com.frolfr.api.model.Round
import com.frolfr.config.PaginationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RoundsViewModel : ViewModel() {

    companion object {
        private const val PAGE_SIZE = PaginationConfig.DEFAULT_PAGE_SIZE
    }
    private var fetchedPages = 1
    private var hasNextPage = true

    val rounds = MutableLiveData<List<Round>>().apply {
        value = emptyList()
    }

    private val _navigateToRoundDetail = MutableLiveData<Round>()
    val navigateToRoundDetail
        get() = _navigateToRoundDetail

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        loadRoundsPage(1)
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

    fun loadRoundsPage(pageNum: Int) {
        coroutineScope.launch {
            try {
                val userId = FrolfrAuthorization.userId
                val roundsDocument = FrolfrApi.retrofitService.rounds(pageNum, PAGE_SIZE, userId)

                rounds.value = rounds.value?.plus(roundsDocument)

                val paginationLinks = roundsDocument.links.get<PaginationLinks>(PaginationLinksAdapter()) as PaginationLinks
                hasNextPage = paginationLinks.hasNextPage()
            } catch (t: Throwable) {
                Log.i("frolfrRounds", "Got error result", t)
            }
        }
    }

}