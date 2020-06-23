package com.frolfr.ui.scorecard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.frolfr.api.model.Course

class ScorecardViewModelFactory(private val scorecardId: Int) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScorecardViewModel::class.java)) {
            return ScorecardViewModel(
                scorecardId = scorecardId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
