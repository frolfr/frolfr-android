package com.frolfr.ui.courses

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

        coursesViewModel.courses.observe(viewLifecycleOwner, Observer { courses ->
            if (courses.isEmpty() && !coursesViewModel.fetchedCourses()) {
                coursesViewModel.fetchCourses()
            } else {
                courseAdapter.submitList(courses)
                if (!coursesViewModel.fetchedAdditionalCourses()) {
                    coursesViewModel.fetchAdditionalCourses()
                }
            }
        })

        coursesViewModel.navigateToCourseDetail.observe(viewLifecycleOwner, Observer { course ->
            course?.let {
                // TODO course detail
//                this.findNavController().navigate(
//                    CoursesFragmentDirections.actionNavCoursesToScorecardFragment(
//                        course.id,
//                        course.course.name,
//                        courseDF.format(course.createdAt)
//                    )
//                )
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

        return binding.root
    }
}