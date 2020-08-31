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
import java.util.*

class RoundRepository {

    // TODO look into Dagger2 to handle DI

    private val apiService = FrolfrApi.retrofitService
    private val dbService = FrolfrDatabase.getInstance()

    private val dbRoundMapper = RoundMapper()
    private val apiRoundMapper = com.frolfr.api.mapper.RoundMapper()

    fun getRounds(): LiveData<List<Round>> {
        val dbRounds = dbService.roundDAO.getAllRoundsFull()
        return Transformations.map(dbRounds) { dbRounds ->
            dbRounds.map { dbRound ->
                dbRoundMapper.fromModel(dbRound)
            }
        }
    }

    suspend fun fetchAllRounds() {
        var page = 1
        do {
            Log.i("getRounds", "Fetching page $page of rounds from API")
            val roundsDocument = apiService.rounds(page++, 15, FrolfrAuthorization.userId)
            val paginationLinks = roundsDocument.links
                .get<PaginationLinks>(PaginationLinksAdapter()) as PaginationLinks

            val rounds = roundsDocument.map { apiRoundMapper.toDomain(it) }
            val dbRoundsFull = rounds.map { dbRoundMapper.toModel(it) }
            val dbCourses = dbRoundsFull.map { it.course }
            val dbUserScorecardsFull = dbRoundsFull.map { it.userScorecards }.flatten()
            val dbUsers = dbUserScorecardsFull.map { it.user }
            val dbUserScorecards = dbUserScorecardsFull.map { it.userScorecard }
            val dbTurns = dbUserScorecardsFull.map { it.turns }.flatten()
            val dbRounds = dbRoundsFull.map { it.round }

            dbService.courseDAO.insert(dbCourses)
            dbService.userDAO.insert(dbUsers)
            dbService.userScorecardDAO.insert(dbUserScorecards)
            dbService.turnDAO.insert(dbTurns)
            dbService.roundDAO.insert(dbRounds)
        } while (paginationLinks.hasNextPage())
    }

    private fun fetchRoundsSince(date: Date) {

    }

}