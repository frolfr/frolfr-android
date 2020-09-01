package com.frolfr.ui.course

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.frolfr.R
import com.frolfr.databinding.FragmentCourseBinding

class CourseFragment : Fragment() {

    private lateinit var courseViewModel: CourseViewModel

    private lateinit var binding: FragmentCourseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_course, container, false
        )

        val courseId = arguments!!.getInt("courseId")

        courseViewModel =
            ViewModelProviders.of(this, CourseViewModelFactory(courseId))
                .get(CourseViewModel::class.java)

        binding.courseViewModel = courseViewModel

        return binding.root
    }

}