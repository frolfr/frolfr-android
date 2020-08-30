package com.frolfr.domain

import com.frolfr.db.FrolfrDatabase
import com.frolfr.db.mapper.RoundMapper

class RoundBL(val db: FrolfrDatabase) {

    fun insertRound(round: Round) {
        val roundWithRelations = RoundMapper().toModel(round)
        db.courseDAO.insert(roundWithRelations.course)
        db.roundDAO.insert(roundWithRelations.round)
        // TODO scorecards
    }

}