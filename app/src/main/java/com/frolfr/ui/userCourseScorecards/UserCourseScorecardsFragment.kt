package com.frolfr.ui.userCourseScorecards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.frolfr.R
import com.frolfr.api.model.Course
import com.frolfr.databinding.FragmentUserCourseScorecardsBinding

class UserCourseScorecardsFragment : Fragment() {

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

        // Get args using by navArgs property delegate
        val fragmentArgs by navArgs<UserCourseScorecardsFragmentArgs>()

        userCourseScorecardsViewModel =
            ViewModelProviders.of(this).get(UserCourseScorecardsViewModel::class.java)

        binding.userCourseScorecardsViewModel = userCourseScorecardsViewModel

        // TODO fetch data from API
        userCourseScorecardsViewModel.course.value = Course(fragmentArgs.courseId, "ATL", "GA", "US", "Perkerson Park", "complete", "ATL, GA", emptyList(), "2020-05-24T14:27:32.068Z", emptyList(), emptyList(), 18)
        userCourseScorecardsViewModel.userScorecards.value = emptyList()

        val userScorecardAdapter = UserScorecardAdapter(UserScorecardClickListener {
            roundId -> userCourseScorecardsViewModel.onUserScorecardClicked(roundId)
        })

        userCourseScorecardsViewModel.userScorecards.observe(viewLifecycleOwner, Observer {
            userScorecardAdapter.submitList(userCourseScorecardsViewModel.userScorecards.value)
        })

        // TODO
//        userCourseScorecardsViewModel.navigateToRoundDetail.observe(viewLifecycleOwner, Observer { roundId ->
//            roundId.let {
//                this.findNavController().navigate(/*TODO*/)
//                userCourseScorecardsViewModel.onRoundNavigated()
//            }
//        })

        binding.scorecardList.adapter = userScorecardAdapter

        return binding.root
    }

}