package com.frolfr.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.frolfr.api.FrolfrApi
import com.frolfr.api.PaginationLinksAdapter
import com.frolfr.api.model.PaginationLinks
import com.frolfr.db.FrolfrDatabase
import com.frolfr.db.mapper.RoundMapper
import com.frolfr.db.model.ApiSyncEntity
import com.frolfr.db.model.EntityType
import com.frolfr.domain.model.Course
import com.frolfr.domain.model.Round
import com.frolfr.domain.model.User
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

    fun getCourseRounds(courseId: Int): LiveData<List<Round>> {
        val dbRounds = dbService.roundDAO.getAllRoundsWithRelationsForCourse(courseId)
        return Transformations.map(dbRounds) { dbRounds ->
            dbRounds.map { dbRound ->
                dbRoundMapper.fromModel(dbRound)
            }
        }
    }

    suspend fun fetchAllRounds() {
        var roundsSync = dbService.apiSyncDAO.get(EntityType.ROUND) ?: ApiSyncEntity(EntityType.ROUND)
        var page = 1
        do {
            val roundsDocument = apiService.rounds(page++)
            val paginationLinks = roundsDocument.links
                .get<PaginationLinks>(PaginationLinksAdapter()) as PaginationLinks

            val rounds = roundsDocument.map { apiRoundMapper.toDomain(it) }

            if (rounds.isNotEmpty()) {
                roundsSync.minId = roundsSync.minId.coerceAtMost(rounds.first().id)
                roundsSync.maxId = roundsSync.maxId.coerceAtLeast(rounds.last().id)
            }
            roundsSync.lastSyncedAt = Date().time

            persistRounds(rounds)
            dbService.apiSyncDAO.insert(roundsSync)
        } while (paginationLinks.hasNextPage())

        roundsSync.hasFullHistory = true
        dbService.apiSyncDAO.insert(roundsSync)
    }

    suspend fun fetchMissingRounds() {
        var roundsSync = dbService.apiSyncDAO.get(EntityType.ROUND)
        // TODO can make the `!roundsSync.hasFullHistory` more efficient
        if (roundsSync == null || !roundsSync.hasFullHistory) {
            fetchAllRounds()
        } else {
            fetchRoundsSince(roundsSync.maxId)
        }
    }

    private suspend fun fetchRoundsSince(latestId: Int) {
        var page = 1
        do {
            val roundsDocument = apiService.rounds(page++, sort = "-id")
            val paginationLinks = roundsDocument.links
                .get<PaginationLinks>(PaginationLinksAdapter()) as PaginationLinks

            val rounds = roundsDocument.map { apiRoundMapper.toDomain(it) }
            val roundsSince = rounds.filter { it.id > latestId }
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

    private fun persistRound(round: Round) {
        persistRounds(listOf(round))
    }

    suspend fun fetchRoundsPage(page: Int) {
        val roundsDocument = apiService.rounds(page)

        val rounds = roundsDocument.map { apiRoundMapper.toDomain(it) }
        persistRounds(rounds)
    }

    suspend fun createRound(course: Course, users: List<User>): Round {
        val roundBody = com.frolfr.api.model.Round()
        roundBody.setCourse(course.id)
        roundBody.setUsers(users.map { it.id })
        val apiRound = FrolfrApi.retrofitService.createRound(roundBody)

        val round = apiRoundMapper.toDomain(apiRound)
        persistRound(round)
        return round
    }

    fun markCompleted(roundId: Int) {
        dbService.roundDAO.completeRound(roundId)
        dbService.userScorecardDAO.completeByRound(roundId)
    }

}