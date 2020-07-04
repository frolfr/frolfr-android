package com.frolfr.ui.rounds

//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.fragment.app.Fragment
//import com.frolfr.R
//import com.frolfr.databinding.ViewUserScoreBinding
//
//class UserScoreFragment : Fragment() {
//
//    private lateinit var binding: ViewUserScoreBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = DataBindingUtil.inflate(
//            inflater, R.layout.view_user_score, container, false
//        )
//
//        binding.userId = arguments!!.getInt("userId")
//        binding.avatarUri = arguments!!.getString("avatarUri")
//        binding.score = arguments!!.getInt("score")
//
//        return binding.root
//    }
//}