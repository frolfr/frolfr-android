package com.frolfr.ui.rounds

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.frolfr.R
import com.frolfr.databinding.FragmentRoundsBinding
import com.frolfr.ui.scorecard.ScorecardFragmentDirections
import java.text.DateFormat

class RoundsFragment : Fragment() {

    private lateinit var roundsViewModel: RoundsViewModel

    private lateinit var binding: FragmentRoundsBinding

    private var roundDF = DateFormat.getDateInstance(2)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_rounds, container, false
        )

        roundsViewModel =
            ViewModelProviders.of(this).get(RoundsViewModel::class.java)

        binding.roundsViewModel = roundsViewModel

        val roundAdapter = RoundAdapter(RoundClickListener {
            round -> roundsViewModel.onRoundClicked(round)
        }, roundsViewModel.PageOnListEndListener())

        roundsViewModel.rounds.observe(viewLifecycleOwner, Observer { rounds ->
            if (rounds == null) {
                roundsViewModel.fetchRounds()
            } else {
                roundAdapter.submitList(rounds)
            }
        })

        roundsViewModel.navigateToRoundDetail.observe(viewLifecycleOwner, Observer { round ->
            round?.let {
                this.findNavController().navigate(
                    RoundsFragmentDirections.actionNavRoundsToScorecardFragment(
                        round.id,
                        round.course.name,
                        roundDF.format(round.createdAt)
                    )
                )
                roundsViewModel.onRoundNavigated()
            }
        })

        binding.viewRounds.adapter = roundAdapter

        binding.swiperefreshRounds.setOnRefreshListener {
            roundsViewModel.refreshRounds()
        }
        roundsViewModel.refreshComplete.observe(viewLifecycleOwner, Observer { refreshComplete ->
            if (refreshComplete) {
                binding.swiperefreshRounds.isRefreshing = false
                roundsViewModel.onRefreshCompleteAcknowledged()
                binding.viewRounds.layoutManager?.scrollToPosition(0)
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.rounds, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh_rounds -> {
                binding.swiperefreshRounds.isRefreshing = true
                roundsViewModel.refreshRounds()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}