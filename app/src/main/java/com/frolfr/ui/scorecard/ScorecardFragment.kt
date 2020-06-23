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

class ScorecardFragment : Fragment() {

    private lateinit var scorecardViewModel: ScorecardViewModel

    private lateinit var binding: FragmentScorecardBinding

    private var scorecardLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_scorecard, container, false
        )

        val fragmentArgs by navArgs<ScorecardFragmentArgs>()

        scorecardViewModel =
            ViewModelProviders.of(this, ScorecardViewModelFactory(fragmentArgs.scorecardId))
                .get(ScorecardViewModel::class.java)

        binding.scorecardViewModel = scorecardViewModel

        scorecardViewModel.scorecard.observe(viewLifecycleOwner, Observer {scorecard ->
            if (!scorecardLoaded) {
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
        for (i in 1..numSections) {
            val scorecardSectionFragment = ScorecardSectionFragment(scorecardViewModel, i)
            txn.add(binding.layoutScorecard.id, scorecardSectionFragment)
        }
        txn.commit()
    }
}