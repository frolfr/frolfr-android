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
        val dbRounds = dbService.roundDAO.getAllRoundsWithRelations()
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
            persistRounds(rounds)
        } while (paginationLinks.hasNextPage())
    }

    suspend fun fetchRoundsSince(date: Date) {
        var page = 1
        do {
            Log.i("getRoundsSince", "Fetching page $page of rounds from API")
            val roundsDocument = apiService.rounds(page++, 15, FrolfrAuthorization.userId)
            val paginationLinks = roundsDocument.links
                .get<PaginationLinks>(PaginationLinksAdapter()) as PaginationLinks

            val rounds = roundsDocument.map { apiRoundMapper.toDomain(it) }
            val roundsSince = rounds.filter { it.createdAt > date }
            val endReached = roundsSince.isEmpty() || roundsSince.size < rounds.size
            persistRounds(roundsSince)
        } while (paginationLinks.hasNextPage() && !endReached)
    }

    private fun persistRounds(rounds: List<Round>) {
        if (rounds.isEmpty()) return

        val dbRoundsFull = rounds.map { dbRoundMapper.toModel(it) }
        val dbRounds = dbRoundsFull.map { it.round }
        val dbCourses = dbRoundsFull.map { it.course }.distinctBy { it.id }
        val dbUserScorecardsFull = dbRoundsFull.map { it.userScorecards }.flatten()
        val dbUsers = dbUserScorecardsFull.map { it.user }.distinctBy { it.id }
        var dbUserScorecards = dbUserScorecardsFull.map { it.userScorecard }
        val dbTurns = dbUserScorecardsFull.map { it.turns }.flatten()

        dbService.runInTransaction {
            dbService.courseDAO.insert(dbCourses)
            dbService.userDAO.insert(dbUsers)
            dbService.userScorecardDAO.insert(dbUserScorecards)
            dbService.turnDAO.insert(dbTurns)
            dbService.roundDAO.insert(dbRounds)
        }
    }

    suspend fun fetchRoundsPage(page: Int) {
        Log.i("getRounds", "Fetching page $page of rounds from API")
        val roundsDocument = apiService.rounds(page, 15, FrolfrAuthorization.userId)

        val rounds = roundsDocument.map { apiRoundMapper.toDomain(it) }
        persistRounds(rounds)
    }

}