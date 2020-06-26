package com.frolfr.ui.scorecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.frolfr.R
import com.frolfr.databinding.FragmentScorecardBinding
import kotlin.math.ceil
import kotlin.properties.Delegates


class ScorecardFragment : Fragment() {

    private lateinit var scorecardViewModel: ScorecardViewModel

    private lateinit var binding: FragmentScorecardBinding

    private var scorecardLoaded = false

    private var scorecardId by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_scorecard, container, false
        )

        val fragmentArgs by navArgs<ScorecardFragmentArgs>()

        scorecardId = fragmentArgs.scorecardId

        scorecardViewModel =
            ViewModelProviders.of(this, ScorecardViewModelFactory(scorecardId))
                .get(ScorecardViewModel::class.java)

        binding.scorecardViewModel = scorecardViewModel

        scorecardViewModel.scorecard.observe(viewLifecycleOwner, Observer {scorecard ->
            if (savedInstanceState == null && !scorecardLoaded) {
                loadScorecardSections(scorecard)
                scorecardLoaded = true
            }
        })

        return binding.root
    }

    private fun loadScorecardSections(scorecard: Scorecard) {
        val numSections = ceil(scorecard.holeMeta.size / 9.0).toInt()
        val txn = childFragmentManager
            .beginTransaction()

        scorecard.users.forEach { (userId, user) ->
            val userScorecardSummaryFragment = UserScorecardSummaryFragment()

            val args = Bundle()
            args.putInt("userId", userId)
            userScorecardSummaryFragment.arguments = args

            txn.add(binding.layoutScorecard.id, userScorecardSummaryFragment)
        }

        for (i in 1..numSections) {
            val scorecardSectionFragment = ScorecardSectionFragment()

            val args = Bundle()
            args.putInt("scorecardId", scorecardId)
            args.putInt("sectionIndex", i)
            scorecardSectionFragment.arguments = args

            txn.add(binding.layoutScorecard.id, scorecardSectionFragment)
        }

        txn.commit()
    }
}