package com.frolfr.ui.courses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.frolfr.R
import com.frolfr.api.FrolfrApi
import com.frolfr.api.model.Course
import com.frolfr.databinding.FragmentCoursesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

        val courseAdapter = CourseAdapter(coursesViewModel.PageOnListEndListener())

        // TODO needed, or can I use binding?
        coursesViewModel.courses.observe(viewLifecycleOwner, Observer {
            courseAdapter.submitList(coursesViewModel.courses.value)
        })

        binding.userCoursesList.adapter = courseAdapter
        binding.userCoursesList.setOnClickListener { view ->
            // TODO is this for each item, or the view group?
        }

        return binding.root
    }
}