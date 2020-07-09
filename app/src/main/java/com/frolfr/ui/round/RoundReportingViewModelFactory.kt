package com.frolfr.ui.round

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RoundReportingViewModelFactory(private val roundId: Int) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoundReportingViewModel::class.java)) {
            return RoundReportingViewModel(
                roundId = roundId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
