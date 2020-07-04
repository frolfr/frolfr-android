package com.frolfr.ui.scorecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.frolfr.R
import com.frolfr.databinding.FragmentScorecardSectionBinding
import kotlin.properties.Delegates

class ScorecardSectionFragment() : Fragment() {

    private lateinit var scorecardViewModel: ScorecardViewModel

    private lateinit var binding: FragmentScorecardSectionBinding

    private var roundId by Delegates.notNull<Int>()
    private var sectionIndex by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_scorecard_section, container, false
        )

        roundId = arguments!!.getInt("roundId")
        sectionIndex = arguments!!.getInt("sectionIndex")

        scorecardViewModel =
            ViewModelProviders.of(parentFragment!!, ScorecardViewModelFactory(roundId))
                .get(ScorecardViewModel::class.java)

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

        if (savedInstanceState == null) {
            loadSectionScores()
        }

        return binding.root
    }

    private fun loadSectionScores() {

        val txn = childFragmentManager.beginTransaction()

        scorecardViewModel.scorecard.value?.users?.forEach { user ->
            val userScorecardSectionResultsFragment = UserScorecardSectionResultsFragment()

            val args = Bundle()
            args.putInt("roundId", roundId)
            args.putInt("sectionIndex", sectionIndex)
            args.putInt("userId", user.key)
            userScorecardSectionResultsFragment.arguments = args

            txn.add(binding.layoutUserScorecardSectionResults.id, userScorecardSectionResultsFragment)
        }

        txn.commit()
    }
}