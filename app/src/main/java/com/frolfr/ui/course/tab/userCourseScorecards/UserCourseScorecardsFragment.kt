package com.frolfr.ui.course.tab.userCourseScorecards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.frolfr.R
import com.frolfr.databinding.FragmentUserCourseScorecardsBinding
import com.frolfr.ui.course.CourseFragmentDirections

// TODO implement a CourseTabFragment interface
class UserCourseScorecardsFragment(
    private val courseId: Int,
    private val courseName: String
) : Fragment() {
    // TODO
    constructor() : this(0, "TODO")

    private lateinit var userCourseScorecardsViewModel: UserCourseScorecardsViewModel
    private lateinit var binding: FragmentUserCourseScorecardsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_user_course_scorecards, container, false
        )

        userCourseScorecardsViewModel =
            ViewModelProviders.of(this, UserCourseScorecardsViewModelFactory(courseId))
                .get(UserCourseScorecardsViewModel::class.java)

        binding.userCourseScorecardsViewModel = userCourseScorecardsViewModel

        userCourseScorecardsViewModel.userScorecards.value = emptyList()

        val userScorecardAdapter = UserScorecardAdapter(UserScorecardClickListener {
            roundId -> userCourseScorecardsViewModel.onUserScorecardClicked(roundId)
        })

        userCourseScorecardsViewModel.userScorecards.observe(viewLifecycleOwner, Observer {
            userScorecardAdapter.submitList(userCourseScorecardsViewModel.userScorecards.value)
        })

        userCourseScorecardsViewModel.navigateToRoundDetail.observe(viewLifecycleOwner, Observer { scorecardId ->
            scorecardId?.let {
                this.findNavController().navigate(
                    CourseFragmentDirections.actionCourseFragmentToScorecardFragment(
                        scorecardId, courseName))
                userCourseScorecardsViewModel.onRoundNavigated()
            }
        })

        binding.scorecardList.adapter = userScorecardAdapter

        return binding.root
    }

}