package com.frolfr.ui.course.overview

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
import com.frolfr.databinding.FragmentCourseOverviewBinding

class CourseOverviewFragment : Fragment() {

    private lateinit var courseOverviewViewModel: CourseOverviewViewModel

    private lateinit var binding: FragmentCourseOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_course_overview, container, false
        )
        binding.lifecycleOwner = this

        val courseId = arguments!!.getInt("courseId")

        courseOverviewViewModel =
            ViewModelProviders.of(this.parentFragment!!, CourseOverviewViewModelFactory(courseId))
                .get(CourseOverviewViewModel::class.java)

        binding.courseOverviewViewModel = courseOverviewViewModel

        return binding.root
    }

}