package com.frolfr.ui.scorecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.frolfr.R
import com.frolfr.databinding.FragmentUserScorecardSummaryBinding
import kotlin.properties.Delegates

class UserScorecardSummaryFragment(): Fragment() {

    private lateinit var scorecardViewModel: ScorecardViewModel

    private lateinit var binding: FragmentUserScorecardSummaryBinding

    private var userId by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_user_scorecard_summary, container, false
        )

        val scorecardId = arguments!!.getInt("scorecardId")
        userId = arguments!!.getInt("userId")

        scorecardViewModel =
            ViewModelProviders.of(parentFragment!!, ScorecardViewModelFactory(scorecardId))
                .get(ScorecardViewModel::class.java)

        binding.scorecardViewModel = scorecardViewModel
        binding.userId = userId

        return binding.root
    }

}