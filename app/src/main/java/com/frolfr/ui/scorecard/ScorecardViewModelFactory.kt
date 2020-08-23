package com.frolfr.ui.scorecard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ScorecardViewModelFactory(private val roundId: Int) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScorecardViewModel::class.java)) {
            return ScorecardViewModel(
                roundId = roundId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
