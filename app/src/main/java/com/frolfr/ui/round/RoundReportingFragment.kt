package com.frolfr.ui.round

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
        binding.setLifecycleOwner { lifecycle } // TODO why don't I need this elsewhere?

        val fab: FloatingActionButton = activity!!.findViewById(R.id.fab)
        fab.hide()

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
            if (binding.spinnerHole.selectedItem != currentHole) {
                binding.spinnerHole.setSelection(currentHole - 1)
            }
        })

        viewModel.currentPar.observe(viewLifecycleOwner, Observer { currentPar ->
            if (binding.spinnerPar.selectedItem != currentPar) {
                binding.spinnerPar.setSelection(currentPar - 3)
            }
        })

        viewModel.round.observe(viewLifecycleOwner, Observer { round ->
            if (round != null) {
                val users = round.getUsers()

                val userHoleInputsLayout = binding.layoutUserHoleInputs
                users.forEach { user ->
                    val userViewBinding: ViewUserHoleInputBinding = DataBindingUtil.inflate(
                        inflater, R.layout.view_user_hole_input, userHoleInputsLayout, true
                    )
                    userViewBindings.add(userViewBinding)
                    userViewBinding.userStrokes = viewModel.currentUserStrokes
                    userViewBinding.userId = user.id.toInt()
                    userViewBinding.buttonStrokesMinus.setOnClickListener {
                        viewModel.onStrokesMinusClicked(user.id.toInt())
                    }
                    userViewBinding.buttonStrokesPlus.setOnClickListener {
                        viewModel.onStrokesPlusClicked(user.id.toInt())
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

                val holeCount = round.getCourse().holeCount
                val holeArray = Array(holeCount) { i -> i + 1 }
                val holeAdapter = ArrayAdapter<Int>(context!!, R.layout.layout_hole_selected_item, holeArray)
                holeAdapter.setDropDownViewResource(R.layout.layout_hole_list_item)
                binding.spinnerHole.adapter = holeAdapter
                binding.spinnerHole.onItemSelectedListener = HoleChangeListener(viewModel)

                val parArray = Array(3) { i -> i + 3 }
                val parAdapter = ArrayAdapter<Int>(context!!, R.layout.layout_par_selected_item, parArray)
                parAdapter.setDropDownViewResource(R.layout.layout_par_list_item)
                binding.spinnerPar.adapter = parAdapter
                binding.spinnerPar.onItemSelectedListener = ParChangeListener(viewModel)
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

    class HoleChangeListener(private val viewModel: RoundReportingViewModel) : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val holeNumber = parent!!.getItemAtPosition(position) as Int
            viewModel.setHole(holeNumber)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    class ParChangeListener(private val viewModel: RoundReportingViewModel) : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val parNumber = parent!!.getItemAtPosition(position) as Int
            viewModel.setPar(parNumber)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    override fun onPause() {
        super.onPause()

        val fab: FloatingActionButton = activity!!.findViewById(R.id.fab)
        fab.show()
    }
}