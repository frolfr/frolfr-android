package com.frolfr.domain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.frolfr.api.FrolfrApi
import com.frolfr.api.FrolfrAuthorization
import com.frolfr.api.PaginationLinksAdapter
import com.frolfr.api.model.PaginationLinks
import com.frolfr.db.FrolfrDatabase
import com.frolfr.db.mapper.RoundMapper
import com.frolfr.domain.model.Round
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class RoundRepository {

    // TODO look into Dagger2 to handle DI

    private val apiService = FrolfrApi.retrofitService
    private val dbService = FrolfrDatabase.getInstance()
    private val dbRoundMapper = RoundMapper()
    private val apiRoundMapper = com.frolfr.api.mapper.RoundMapper()

    private var job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.IO)

    fun getRounds(): LiveData<List<Round>> {
        // TODO get rounds from db. If no rounds, fetch from API and store to db, then get
        val dbRounds = dbService.roundDAO.getAllRoundsFull()
        val latestRound = dbRounds.value?.getOrNull(0)
        if (latestRound == null) {
            Log.i("getRounds", "No rounds in db, fetching from API")
            coroutineScope.launch {
                fetchAllRounds()
            }
        } else {
            fetchRoundsSince(Date(latestRound?.round.createdAt))
            // TODO may need to fetch older rounds if fetchAllRounds wasn't allowed to complete...
        }
        Log.i("getRounds", "Converting ${dbRounds.value?.size ?: 0} db rounds to domain")
        return Transformations.map(dbRounds) { dbRounds ->
            dbRounds.map { dbRound ->
                Log.i("getRounds", "Mapping a db round")
                dbRoundMapper.fromModel(dbRound)
            }
        }
    }

    private suspend fun fetchAllRounds() {
        var page = 1
        do {
            Log.i("getRounds", "Fetching page $page of rounds from API")
            val roundsDocument = apiService.rounds(page++, 15, FrolfrAuthorization.userId)
            val paginationLinks = roundsDocument.links
                .get<PaginationLinks>(PaginationLinksAdapter()) as PaginationLinks
            val rounds = roundsDocument.map { apiRoundMapper.toDomain(it) }
            val dbRoundsFull = rounds.map { dbRoundMapper.toModel(it) }
            val dbCourses = dbRoundsFull.map { it.course }
            val dbRounds = dbRoundsFull.map { it.round }
            dbService.courseDAO.insert(dbCourses)
            dbService.roundDAO.insert(dbRounds)
        } while (paginationLinks.hasNextPage())
    }

    private fun fetchRoundsSince(date: Date) {

    }

}