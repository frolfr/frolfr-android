package com.frolfr.ui.round

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.frolfr.R
import com.frolfr.databinding.FragmentHoleInputBinding

class RoundReportingFragment : Fragment() {

    private lateinit var binding: FragmentHoleInputBinding

    private lateinit var viewModel: RoundReportingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_hole_input, container, false
        )

        val roundId = arguments!!.getInt("roundId")

        viewModel =
            ViewModelProviders.of(this, RoundReportingViewModelFactory(roundId))
                .get(RoundReportingViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.round.observe(viewLifecycleOwner, Observer { round ->
            binding.textHoleParNumber.text = round.toString()   // TODO
        })

        return binding.root
    }
}