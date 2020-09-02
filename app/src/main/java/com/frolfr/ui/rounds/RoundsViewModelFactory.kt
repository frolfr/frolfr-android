package com.frolfr.ui.rounds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RoundsViewModelFactory(private val roundRestrictions: RoundRestrictions) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoundsViewModel::class.java)) {
            return RoundsViewModel(
                roundRestrictions = roundRestrictions
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
