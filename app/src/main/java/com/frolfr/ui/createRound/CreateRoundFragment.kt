package com.frolfr.ui.createRound

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.frolfr.R
import com.frolfr.api.model.User
import com.frolfr.api.model.User2
import com.frolfr.databinding.FragmentCreateRoundBinding

class CreateRoundFragment : Fragment() {

    private lateinit var binding: FragmentCreateRoundBinding

    private lateinit var viewModel: CreateRoundViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_round, container, false
        )

        viewModel = ViewModelProviders.of(this).get(CreateRoundViewModel::class.java)

        binding.viewModel = viewModel

        val coursesAdapter = CoursesAdapter(context!!)
        binding.spinnerCourses.adapter = coursesAdapter

        viewModel.courses.observe(viewLifecycleOwner, Observer { courseList ->
            coursesAdapter.addAll(courseList)
        })

        val playersAdapter = UsersAdapter(context!!)
        binding.editTextPlayer.setAdapter(playersAdapter)

        val playerSelectedListener = PlayerSelectedListener { user ->
            viewModel.addUser(user)
            binding.editTextPlayer.setText("")
        }
        binding.editTextPlayer.onItemSelectedListener = playerSelectedListener
        binding.editTextPlayer.onItemClickListener = playerSelectedListener

        viewModel.users.observe(viewLifecycleOwner, Observer { userList ->
            playersAdapter.addAll(userList)
        })

        viewModel.selectedUsers.observe(viewLifecycleOwner, Observer { selectedUsers ->
            if (selectedUsers.isNotEmpty()) {
                binding.textSelectedPlayers.text = selectedUsers.map { user ->
                    "${user.firstName} ${user.lastName}"
                }.reduce {
                    acc, string -> "${acc}\n${string}"
                }
            } else {
                binding.textSelectedPlayers.text = ""
            }
        })

        binding.buttonStartRound.setOnClickListener {
            findNavController().navigate(CreateRoundFragmentDirections.actionCreateRoundFragmentToRoundReportingFragment())
        }

        return binding.root
    }
}

class PlayerSelectedListener(val selectedListener: (user: User2) -> Unit) :
    AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val user = parent!!.getItemAtPosition(position) as User2
        selectedListener(user)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        return onItemSelected(parent, view, position, id)
    }

}