package com.frolfr.ui.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.frolfr.R
import com.frolfr.databinding.FragmentCourseBinding
import com.google.android.material.tabs.TabLayoutMediator

class CourseFragment : Fragment() {

    private lateinit var courseViewModel: CourseViewModel

    private lateinit var binding: FragmentCourseBinding

    private lateinit var courseTabPagerAdapter: CourseTabPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_course, container, false
        )

        viewPager = binding.viewPagerCourse

        // Get args using by navArgs property delegate
        val fragmentArgs by navArgs<CourseFragmentArgs>()

        courseViewModel =
            ViewModelProviders.of(this, CourseViewModelFactory(fragmentArgs.course))
                .get(CourseViewModel::class.java)

        binding.courseViewModel = courseViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        courseTabPagerAdapter = CourseTabPagerAdapter(binding.courseViewModel!!.course.value!!.id,
            childFragmentManager, lifecycle)
        viewPager.adapter = courseTabPagerAdapter

        val tabLayout = binding.tabLayoutCourse
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // TODO not this
            tab.text = when (position) {
                0 -> getString(R.string.course_tab_overview)
                1 -> getString(R.string.course_tab_scorecards)
                else -> getString(R.string.course_tab_leaderboard)
            }
        }.attach()
    }
}