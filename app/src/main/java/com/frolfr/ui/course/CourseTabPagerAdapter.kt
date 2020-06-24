package com.frolfr.ui.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.frolfr.ui.course.tab.userCourseScorecards.UserCourseScorecardsFragment

class CourseTabPagerAdapter(
    private val courseId: Int,
    private val courseName: String,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        val userCourseScorecardsFragment = UserCourseScorecardsFragment()

        val args = Bundle()
        args.putInt("courseId", courseId)
        args.putString("courseName", courseName)
        userCourseScorecardsFragment.arguments = args

        return userCourseScorecardsFragment

        // TODO pass in fragments or a factory via constructor?
//        return when (position) {
//            1 -> UserCourseScorecardsFragment()
//            else -> TODO("Not yet implemented")
//        }
    }



}