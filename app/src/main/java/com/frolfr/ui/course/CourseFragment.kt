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
import com.frolfr.ui.course.leaderboard.CourseLeaderboardFragment
import com.frolfr.ui.course.overview.CourseOverviewFragment
import com.frolfr.ui.course.rounds.CourseRoundsFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

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
        binding.lifecycleOwner = this   // TODO need this here? where else?

        val courseId = arguments!!.getInt("courseId")

        courseViewModel =
            ViewModelProviders.of(this, CourseViewModelFactory(courseId))
                .get(CourseViewModel::class.java)

        binding.courseViewModel = courseViewModel

        // TODO Why must I observe to get the LiveData returned from Room to get set?
        //      Additionally, if I try to return a pure entity from the DAO, not LiveData,
        //      the course isn't populated on first load (subsequent ones it shows up...)
        // See NOTE above on setting the binding lifecycleOwner
//        courseViewModel.course.observe(this, Observer { course ->
//            if (course != null) {
//                requireActivity().title = course.name
//            }
//        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val courseTabsAdapter = CourseTabsAdapter(this, binding.courseViewModel!!.courseId)
        val viewPager = binding.viewPagerCourse
        viewPager.adapter = courseTabsAdapter

        val tabLayout = binding.tabLayoutCourse
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabConfig[position].name
        }.attach()
    }

}

data class TabInfo(
    val name: String,
    val fragment: KClass<out Fragment>
)

private val tabConfig = listOf(
    TabInfo("Overview", CourseOverviewFragment::class),
    TabInfo("Rounds", CourseRoundsFragment::class),
    TabInfo("Leaderboard", CourseLeaderboardFragment::class)
)

class CourseTabsAdapter(fragment: Fragment, val courseId: Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = tabConfig[position].fragment.createInstance()
        fragment.arguments = Bundle().apply {
            putInt("courseId", courseId)
        }
        return fragment
    }
}
