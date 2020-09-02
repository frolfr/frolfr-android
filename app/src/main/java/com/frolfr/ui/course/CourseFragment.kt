package com.frolfr.ui.course

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.frolfr.R
import com.frolfr.databinding.FragmentCourseBinding
import com.frolfr.databinding.FragmentCourseOverviewBinding
import com.google.android.material.tabs.TabLayoutMediator

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

        // TODO Why must I observe to get the LiveData returned from Room to get set?
        //      Additionally, if I try to return a pure entity from the DAO, not LiveData,
        //      the course isn't populated on first load (subsequent ones it shows up...)
        courseViewModel.course.observe(this, Observer { })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val courseTabsAdapter = CourseTabsAdapter(this, binding.courseViewModel!!.courseId)
        val viewPager = binding.viewPagerCourse
        viewPager.adapter = courseTabsAdapter

        val tabLayout = binding.tabLayoutCourse
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Overview"
                1 -> "Rounds"
                else -> "Leaderboard"
            }
        }.attach()
    }

}

class CourseTabsAdapter(fragment: Fragment, val courseId: Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = DemoObjectFragment()
        fragment.arguments = Bundle().apply {
            putInt("courseId", courseId)
        }
        return fragment
    }
}

// Instances of this class are fragments representing a single
// object in our collection.
class DemoObjectFragment : Fragment() {

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