package com.frolfr.ui.scorecard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.frolfr.R
import com.frolfr.databinding.FragmentUserScorecardSectionResultsBinding
import kotlin.properties.Delegates

class UserScorecardSectionResultsFragment(): Fragment() {

    private lateinit var scorecardViewModel: ScorecardViewModel

    private lateinit var binding: FragmentUserScorecardSectionResultsBinding

    private var sectionIndex by Delegates.notNull<Int>()
    private var userId by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_user_scorecard_section_results, container, false
        )

        val scorecardId = arguments!!.getInt("scorecardId")
        sectionIndex = arguments!!.getInt("sectionIndex")
        userId = arguments!!.getInt("userId")

        scorecardViewModel =
            ViewModelProviders.of(parentFragment!!.parentFragment!!, ScorecardViewModelFactory(scorecardId))
                .get(ScorecardViewModel::class.java)

        binding.scorecardViewModel = scorecardViewModel
        binding.scorecardSectionIndex = sectionIndex
        binding.userId = userId

        colorizeScores()

        return binding.root
    }

    private fun colorizeScores() {
        // TODO should probably be using a ListAdapter instead of all this...
        binding.textScore1.setBackgroundColor(getBackgroundColorForHole(1))
        binding.textScore2.setBackgroundColor(getBackgroundColorForHole(2))
        binding.textScore3.setBackgroundColor(getBackgroundColorForHole(3))
        binding.textScore4.setBackgroundColor(getBackgroundColorForHole(4))
        binding.textScore5.setBackgroundColor(getBackgroundColorForHole(5))
        binding.textScore6.setBackgroundColor(getBackgroundColorForHole(6))
        binding.textScore7.setBackgroundColor(getBackgroundColorForHole(7))
        binding.textScore8.setBackgroundColor(getBackgroundColorForHole(8))
        binding.textScore9.setBackgroundColor(getBackgroundColorForHole(9))
    }

    private fun getBackgroundColorForHole(holeIndex: Int): Int {
        val holeScore = getHoleScore(holeIndex)
        return when {
            holeScore < -2 -> Color.CYAN
            holeScore == -2 -> Color.BLUE
            holeScore == -1 -> Color.GREEN
            holeScore == 1 -> Color.LTGRAY
            holeScore > 1 -> Color.GRAY
            else -> Color.WHITE
        }
    }

    private fun getHoleScore(holeIndex: Int): Int {
        val score = scorecardViewModel.getUserHoleScore(sectionIndex, holeIndex, userId).toIntOrNull()
        val par = scorecardViewModel.getParStr(sectionIndex, holeIndex).toIntOrNull()
        if (score != null && par != null) {
            return score - par
        }
        return 0
    }

}