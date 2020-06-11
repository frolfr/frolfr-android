package com.frolfr.ui.courseDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.frolfr.R
import com.frolfr.api.model.Course
import com.frolfr.databinding.FragmentCourseDetailBinding

class CourseDetailFragment : Fragment() {

    private lateinit var courseDetailViewModel: CourseDetailViewModel
    private lateinit var binding: FragmentCourseDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_course_detail, container, false
        )

        // Get args using by navArgs property delegate
        val fragmentArgs by navArgs<CourseDetailFragmentArgs>()

        courseDetailViewModel =
            ViewModelProviders.of(this).get(CourseDetailViewModel::class.java)
        courseDetailViewModel.course.value = Course(fragmentArgs.courseId, "Test", "test", "", "Perkerson Park", "", "", emptyList(), "", emptyList(), emptyList(), 18)

        binding.courseDetailViewModel = courseDetailViewModel

        return binding.root
    }

}