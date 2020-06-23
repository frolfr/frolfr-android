package com.frolfr.ui.scorecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.frolfr.R
import com.frolfr.databinding.FragmentScorecardSectionBinding

class ScorecardSectionFragment(private val scorecardViewModel: ScorecardViewModel,
                               private val sectionIndex: Int) : Fragment() {
    // TODO
    constructor(): this(ScorecardViewModel(0), 0)

    private lateinit var binding: FragmentScorecardSectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_scorecard_section, container, false
        )

        binding.scorecardViewModel = scorecardViewModel
        binding.scorecardSectionIndex = sectionIndex
        binding.imageswitcherScorecardSectionHeader.setOnClickListener {
            // TODO animate slide up/down, maybe fade in/out as well
            if (scorecardViewModel.isSectionVisible(sectionIndex)) {
                binding.layoutScorecardSectionBody.visibility = GONE
            } else {
                binding.layoutScorecardSectionBody.visibility = VISIBLE
            }
            scorecardViewModel.toggleSectionVisibility(sectionIndex)
        }

        loadSectionScores()

        return binding.root
    }

    private fun loadSectionScores() {
        val txn = childFragmentManager.beginTransaction()

        scorecardViewModel.scorecard.value!!.users.forEach { user ->
            val userScorecardSectionResultsFragment = UserScorecardSectionResultsFragment(
                scorecardViewModel, sectionIndex, user.value.id)
            txn.add(binding.layoutUserScorecardSectionResults.id, userScorecardSectionResultsFragment)
        }

        txn.commit()
    }
}