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

        val courseAdapter = CourseAdapter(UserCourseListener {
            courseId -> coursesViewModel.onCourseClicked(courseId)
        }, coursesViewModel.PageOnListEndListener())

        // TODO needed, or can I use binding?
        coursesViewModel.courses.observe(viewLifecycleOwner, Observer {
            courseAdapter.submitList(coursesViewModel.courses.value)
        })

        coursesViewModel.navigateToCourseDetail.observe(viewLifecycleOwner, Observer {courseId ->
            courseId?.let {
                this.findNavController().navigate(CoursesFragmentDirections.actionNavCoursesToCourseDetailFragment(courseId))
                coursesViewModel.onCourseNavigated()
            }
        })

        binding.userCoursesList.adapter = courseAdapter

        return binding.root
    }
}