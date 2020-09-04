package com.frolfr.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.frolfr.api.FrolfrApi
import com.frolfr.api.FrolfrAuthorization
import com.frolfr.api.PaginationLinksAdapter
import com.frolfr.api.model.PaginationLinks
import com.frolfr.db.FrolfrDatabase
import com.frolfr.db.mapper.UserMapper
import com.frolfr.db.model.ApiSyncEntity
import com.frolfr.db.model.EntityType
import com.frolfr.domain.model.User
import java.util.*

class UserRepository {

    private val apiService = FrolfrApi.retrofitService
    private val dbService = FrolfrDatabase.getInstance()

    private val dbUserMapper = UserMapper()
    private val apiUserMapper = com.frolfr.api.mapper.UserMapper()

    suspend fun getCurrentUser(): User? {
        val userResponse = FrolfrApi.retrofitService.currentUser()

        FrolfrAuthorization.userId = userResponse.id.toInt()

        return apiUserMapper.toDomain(userResponse)
    }

    fun getUsers(): LiveData<List<User>> {
        val dbUsers = dbService.userDAO.getAll()
        return Transformations.map(dbUsers) { dbUsers ->
            dbUsers.map { dbUser ->
                dbUserMapper.fromModel(dbUser)
            }
        }
    }

    suspend fun fetchAllUsers() {
        var usersSync = dbService.apiSyncDAO.get(EntityType.USER) ?: ApiSyncEntity(EntityType.USER)
        var page = 1
        do {
            val usersDocument = apiService.users(page++)
            val paginationLinks = usersDocument.links
                .get<PaginationLinks>(PaginationLinksAdapter()) as PaginationLinks

            val users = usersDocument.map { apiUserMapper.toDomain(it) }

            usersSync.minId = usersSync.minId.coerceAtMost(users.first().id)
            usersSync.maxId = usersSync.maxId.coerceAtLeast(users.last().id)
            usersSync.lastSyncedAt = Date().time

            persistUsers(users)
            dbService.apiSyncDAO.insert(usersSync)
        } while (paginationLinks.hasNextPage())

        usersSync.hasFullHistory = true
        dbService.apiSyncDAO.insert(usersSync)
    }

    suspend fun fetchMissingUsers() {
        var usersSync = dbService.apiSyncDAO.get(EntityType.USER)
        // TODO can make the `!usersSync.hasFullHistory` more efficient
        if (usersSync == null || !usersSync.hasFullHistory) {
            fetchAllUsers()
        } else {
            fetchUsersSince(usersSync.maxId)
        }
    }

    private suspend fun fetchUsersSince(latestId: Int) {
        var page = 1
        do {
            val usersDocument = apiService.users(page++, sort = "-id")
            val paginationLinks = usersDocument.links
                .get<PaginationLinks>(PaginationLinksAdapter()) as PaginationLinks

            val users = usersDocument.map { apiUserMapper.toDomain(it) }
            val usersSince = users.filter { it.id > latestId }
            val endReached = usersSince.isEmpty() || usersSince.size < users.size

            persistUsers(users)
        } while (paginationLinks.hasNextPage() && !endReached)
    }

    private fun persistUsers(users: List<User>) {
        if (users.isEmpty()) return

        val dbUsers = users.map { dbUserMapper.toModel(it) }

        dbService.userDAO.insert(dbUsers)
    }

    fun getFriends(): LiveData<List<User>> {
        val dbFriends = dbService.userDAO.getAllFriends(FrolfrAuthorization.userId!!)
        return Transformations.map(dbFriends) { dbFriends ->
            dbFriends.map { dbUser ->
                dbUserMapper.fromModel(dbUser)
            }
        }
    }

    fun fetchAllFriends() {
        // TODO("Not yet implemented")
    }

    fun fetchMissingFriends() {
        // TODO("Not yet implemented")
    }

}