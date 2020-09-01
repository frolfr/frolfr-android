package com.frolfr.domain.repository

import com.frolfr.api.FrolfrApi
import com.frolfr.api.FrolfrAuthorization
import com.frolfr.db.FrolfrDatabase
import com.frolfr.db.mapper.UserScorecardMapper
import java.util.*

class ScorecardRepository {

    private val apiService = FrolfrApi.retrofitService
    private val dbService = FrolfrDatabase.getInstance()

    private val dbUserScorecardMapper = UserScorecardMapper()
    private val apiUserScorecardMapper = com.frolfr.api.mapper.ScorecardMapper()

    fun getLastPlayedByCourse(): Map<Int, Date> {
        val lastPlayedPerCourse = dbService.userScorecardDAO.getLastPlayedPerCourse(FrolfrAuthorization.userId!!)
        return lastPlayedPerCourse
            .associateBy { it.courseId }
            .mapValues { Date(it.value.lastPlayed) }
    }

}