package com.frolfr.ui.courses

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.frolfr.R
import com.frolfr.databinding.FragmentCoursesBinding

class CoursesFragment : Fragment() {

    private lateinit var coursesViewModel: CoursesViewModel

    private lateinit var binding: FragmentCoursesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_courses, container, false
        )

        coursesViewModel =
            ViewModelProviders.of(this).get(CoursesViewModel::class.java)

        binding.coursesViewModel = coursesViewModel

        val courseAdapter = CourseAdapter(CourseClickListener {
                course -> coursesViewModel.onCourseClicked(course)
        })

        coursesViewModel.coursesWithLastPlayed.observe(viewLifecycleOwner, Observer { coursesWithLastPlayed ->
            if (coursesWithLastPlayed.isEmpty() && !coursesViewModel.fetchedCourses()) {
                coursesViewModel.fetchCourses()
            } else {
                courseAdapter.submitList(coursesWithLastPlayed)
                if (!coursesViewModel.fetchedAdditionalCourses()) {
                    coursesViewModel.fetchAdditionalCourses()
                }
            }
        })
        coursesViewModel.courses.observe(viewLifecycleOwner, Observer { courses ->
            if (courses.isNotEmpty()) {
                coursesViewModel.loadCoursesWithLastPlayed()
            }
        })

        coursesViewModel.navigateToCourseDetail.observe(viewLifecycleOwner, Observer { course ->
            course?.let {
                this.findNavController().navigate(
                    CoursesFragmentDirections.actionNavCoursesToCourseFragment(
                        course.id
                    )
                )
                coursesViewModel.onCourseNavigated()
            }
        })

        binding.viewCourses.adapter = courseAdapter

        binding.swiperefreshCourses.setOnRefreshListener {
            coursesViewModel.refreshCourses()
        }
        coursesViewModel.refreshComplete.observe(viewLifecycleOwner, Observer { refreshComplete ->
            if (refreshComplete) {
                binding.swiperefreshCourses.isRefreshing = false
                coursesViewModel.onRefreshCompleteAcknowledged()
                binding.viewCourses.layoutManager?.scrollToPosition(0)
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.courses, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_played -> {
                coursesViewModel.toggleShowHideUnplayed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}