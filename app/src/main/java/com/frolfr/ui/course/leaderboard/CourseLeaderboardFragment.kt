package com.frolfr.ui.course.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.frolfr.R
import com.frolfr.databinding.FragmentCourseLeaderboardBinding

class CourseLeaderboardFragment : Fragment() {

//    private lateinit var courseViewModel: CourseViewModel

    private lateinit var binding: FragmentCourseLeaderboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_course_leaderboard, container, false
        )

//        val courseId = arguments!!.getInt("courseId")
//
//        courseViewModel =
//            ViewModelProviders.of(this.parentFragment!!, CourseViewModelFactory(courseId))
//                .get(CourseViewModel::class.java)
//
//        binding.courseViewModel = courseViewModel

        return binding.root
    }

}