package com.frolfr.ui.round

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.frolfr.R
import com.frolfr.databinding.FragmentHoleInputBinding
import com.frolfr.databinding.ViewUserHoleInputBinding

class RoundReportingFragment : Fragment() {

    private lateinit var binding: FragmentHoleInputBinding
    private var userViewBindings: MutableList<ViewUserHoleInputBinding> = mutableListOf()

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

        binding.buttonPreviousHole.setOnClickListener {
            viewModel.onPreviousHoleClicked()
        }

        binding.buttonSubmitHole.setOnClickListener {
            viewModel.onSubmitHoleClicked()
        }

        viewModel.currentHole.observe(viewLifecycleOwner, Observer { currentHole ->
            binding.buttonPreviousHole.isEnabled = currentHole != 1
            binding.viewModel = viewModel   // TODO not this
            userViewBindings.forEach {
                it.viewModel = viewModel    // TODO plz not this either
            }
        })

        viewModel.round.observe(viewLifecycleOwner, Observer { round ->
            val users = round.getUsers()

            val userHoleInputsLayout = binding.layoutUserHoleInputs
            users.forEach { user ->
                val userViewBinding: ViewUserHoleInputBinding = DataBindingUtil.inflate(
                    inflater, R.layout.view_user_hole_input, userHoleInputsLayout, true
                )
                userViewBindings.add(userViewBinding)
                userViewBinding.viewModel = viewModel
                userViewBinding.userId = user.id.toInt()
                userViewBinding.buttonStrokesMinus.setOnClickListener {
                    viewModel.onStrokesMinusClicked(user.id.toInt())
                    userViewBinding.viewModel = viewModel   // TODO not this
                }
                userViewBinding.buttonStrokesPlus.setOnClickListener {
                    viewModel.onStrokesPlusClicked(user.id.toInt())
                    userViewBinding.viewModel = viewModel   // TODO not this
                }
                userViewBinding.textUserName.text = user.getName()
                Glide.with(binding.root.context).load(user.avatarUrl?.toUri())
                    .apply(
                        RequestOptions()
                            .circleCrop()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_image)
                    ).into(userViewBinding.imageUserAvatar)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            if (error != null) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                viewModel.clearErrors()
            }
        })

        viewModel.navigateToRoundScorecard.observe(viewLifecycleOwner, Observer { roundId ->
            roundId?.let {
                this.findNavController().navigate(
                    RoundReportingFragmentDirections.actionRoundReportingFragmentToScorecardFragment(roundId)
                )
                viewModel.onRoundScorecardNavigated()
            }
        })

        return binding.root
    }
}