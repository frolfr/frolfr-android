package com.frolfr.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.frolfr.R
import com.frolfr.databinding.FragmentCoursesBinding
import com.frolfr.databinding.FragmentFriendsBinding
import com.frolfr.ui.courses.CourseAdapter
import com.frolfr.ui.courses.CourseClickListener
import com.frolfr.ui.courses.CoursesFragmentDirections
import com.frolfr.ui.courses.CoursesViewModel

class FriendsFragment : Fragment() {

    private lateinit var friendsViewModel: FriendsViewModel

    private lateinit var binding: FragmentFriendsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_friends, container, false
        )

        friendsViewModel =
            ViewModelProviders.of(this).get(FriendsViewModel::class.java)

        binding.friendsViewModel = friendsViewModel

        val friendAdapter = FriendAdapter(FriendClickListener {
            friend -> friendsViewModel.onFriendClicked(friend)
            Toast.makeText(context, "Hehe ${friend.nameFirst} is your friend", Toast.LENGTH_SHORT).show()
        })

        binding.viewFriends.adapter = friendAdapter

        friendsViewModel.friends.observe(viewLifecycleOwner, Observer { friends ->
            if (friends.isEmpty() && !friendsViewModel.fetchedFriends()) {
                friendsViewModel.fetchFriends()
            } else {
                friendAdapter.submitList(friends)
                if (!friendsViewModel.fetchedAdditionalFriends()) {
                    friendsViewModel.fetchAdditionalFriends()
                }
            }
        })

        binding.swiperefreshFriends.setOnRefreshListener {
            friendsViewModel.refreshFriends()
        }
        friendsViewModel.refreshComplete.observe(viewLifecycleOwner, Observer { refreshComplete ->
            if (refreshComplete) {
                binding.swiperefreshFriends.isRefreshing = false
                friendsViewModel.onRefreshCompleteAcknowledged()
                binding.viewFriends.layoutManager?.scrollToPosition(0)
            }
        })

        return binding.root
    }
}
