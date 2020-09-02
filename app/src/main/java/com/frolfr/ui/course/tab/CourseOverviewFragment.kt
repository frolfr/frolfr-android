package com.frolfr.ui.course.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.frolfr.R
import com.frolfr.databinding.FragmentCourseOverviewBinding
import com.frolfr.ui.course.CourseViewModel
import com.frolfr.ui.course.CourseViewModelFactory

class CourseOverviewFragment : Fragment() {

    private lateinit var courseViewModel: CourseViewModel

    private lateinit var binding: FragmentCourseOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_course_overview, container, false
        )

        val courseId = arguments!!.getInt("courseId")

        courseViewModel =
            ViewModelProviders.of(this.parentFragment!!, CourseViewModelFactory(courseId))
                .get(CourseViewModel::class.java)

        binding.courseViewModel = courseViewModel

        return binding.root
    }

}