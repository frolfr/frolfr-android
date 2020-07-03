package com.frolfr.ui.rounds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.frolfr.R
import com.frolfr.databinding.FragmentRoundsBinding

class RoundsFragment : Fragment() {

    private lateinit var roundsViewModel: RoundsViewModel

    private lateinit var binding: FragmentRoundsBinding

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

        roundsViewModel.rounds.observe(viewLifecycleOwner, Observer {
            roundAdapter.submitList(roundsViewModel.rounds.value)
        })

        roundsViewModel.navigateToRoundDetail.observe(viewLifecycleOwner, Observer { round ->
            round?.let {
                // TODO
//                this.findNavController().navigate(RoundsFragmentDirections.actionNavRoundsToRoundFragment(round))
                roundsViewModel.onRoundNavigated()
            }
        })

        binding.viewRounds.adapter = roundAdapter

        return binding.root
    }
}