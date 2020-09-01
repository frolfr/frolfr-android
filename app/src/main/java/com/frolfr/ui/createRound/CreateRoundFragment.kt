package com.frolfr.ui.createRound

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.frolfr.R
import com.frolfr.api.FrolfrAuthorization
import com.frolfr.domain.model.Course
import com.frolfr.domain.model.User
import com.frolfr.databinding.FragmentCreateRoundBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

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

        val fab: FloatingActionButton = activity!!.findViewById(R.id.fab)
        fab.hide()

        val courseSelectedListener = CourseSelectedListener { course ->
            viewModel.selectCourse(course)
        }

        val coursesAdapter = CoursesAdapter(context!!)
        binding.spinnerCourses.adapter = coursesAdapter
        binding.spinnerCourses.onItemSelectedListener = courseSelectedListener

        viewModel.courses.observe(viewLifecycleOwner, Observer { courseList ->

            if (courseList.isEmpty() && !viewModel.fetchedCourses()) {
                viewModel.fetchCourses()
            } else {
                coursesAdapter.clear()
                coursesAdapter.addAll(courseList)
                if (!viewModel.fetchedAdditionalCourses()) {
                    viewModel.fetchAdditionalCourses()
                }
            }
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

            if (userList.isEmpty() && !viewModel.fetchedUsers()) {
                viewModel.fetchUsers()
            } else {
                playersAdapter.clear()
                playersAdapter.addAll(userList)
                if (!viewModel.fetchedAdditionalUsers()) {
                    viewModel.fetchAdditionalUsers()
                }

                // TODO Could/should store the current User itself in FrolfrAuthorization,
                //      in which case we could add the user outside this observer.
                //      Maybe use a different class or move it outside of the api package.
                val currentUser = userList.find { it.id == FrolfrAuthorization.userId }
                if (currentUser != null) {
                    viewModel.addUser(currentUser)
                }
            }
        })

        viewModel.selectedUsers.observe(viewLifecycleOwner, Observer { selectedUsers ->
            if (selectedUsers.isNotEmpty()) {
                binding.textSelectedPlayers.text = selectedUsers.map { user ->
                    "${user.nameFirst} ${user.nameLast}"
                }.reduce {
                    acc, string -> "${acc}\n${string}"
                }
            } else {
                binding.textSelectedPlayers.text = ""
            }
        })

        binding.buttonStartRound.setOnClickListener {
            viewModel.createRound()
        }

        viewModel.round.observe(viewLifecycleOwner, Observer { round ->
            if (round != null) {
                findNavController().navigate(
                    CreateRoundFragmentDirections.actionCreateRoundFragmentToRoundReportingFragment(
                        round.id
                    )
                )
                viewModel.onRoundNavigated()
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            if (error != null) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT)
                viewModel.clearErrors()
            }
        })

        return binding.root
    }

    override fun onPause() {
        super.onPause()

        val fab: FloatingActionButton = activity!!.findViewById(R.id.fab)
        fab.show()
    }
}

class PlayerSelectedListener(val selectedListener: (user: User) -> Unit) :
    AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val user = parent!!.getItemAtPosition(position) as User
        selectedListener(user)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        return onItemSelected(parent, view, position, id)
    }
}

class CourseSelectedListener(val selectedListener: (course: Course?) -> Unit) :
    AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    override fun onNothingSelected(parent: AdapterView<*>?) {
        selectedListener(null)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val course = parent!!.getItemAtPosition(position) as Course
        selectedListener(course)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        return onItemSelected(parent, view, position, id)
    }
}