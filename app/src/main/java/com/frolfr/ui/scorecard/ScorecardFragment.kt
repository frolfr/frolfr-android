package com.frolfr.ui.scorecard

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.frolfr.R
import com.frolfr.databinding.FragmentScorecardBinding
import java.text.DateFormat.getDateInstance
import kotlin.math.ceil
import kotlin.properties.Delegates

class ScorecardFragment : Fragment() {

    private lateinit var scorecardViewModel: ScorecardViewModel

    private lateinit var binding: FragmentScorecardBinding

    private var roundId by Delegates.notNull<Int>()
    private var courseName by Delegates.notNull<String>()
    private var roundDate by Delegates.notNull<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_scorecard, container, false
        )

        val fragmentArgs by navArgs<ScorecardFragmentArgs>()

        roundId = fragmentArgs.roundId
        courseName = fragmentArgs.courseName
        roundDate = fragmentArgs.roundDate

        scorecardViewModel =
            ViewModelProviders.of(this, ScorecardViewModelFactory(roundId))
                .get(ScorecardViewModel::class.java)

        binding.scorecardViewModel = scorecardViewModel

        binding.textCourseName.text = courseName
        binding.textRoundDate.text = roundDate

        scorecardViewModel.scorecard.observe(viewLifecycleOwner, Observer {scorecard ->
            if (savedInstanceState == null) {
                loadContent(scorecard)
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.scorecard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit_round -> {
                 findNavController().navigate(
                    ScorecardFragmentDirections.actionScorecardFragmentToRoundReportingFragment(
                        roundId
                    )
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadContent(scorecard: Scorecard) {
        if (childFragmentManager.fragments.isNotEmpty()) {
            // Already loaded
            return
        }

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
            args.putInt("roundId", roundId)
            args.putInt("sectionIndex", i)
            scorecardSectionFragment.arguments = args

            txn.add(binding.layoutScorecard.id, scorecardSectionFragment)
        }

        txn.commit()
    }
}