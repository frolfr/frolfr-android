package com.frolfr.ui.userCourseScorecards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.frolfr.api.model.UserScorecard
import com.frolfr.databinding.UserScorecardViewBinding

class UserScorecardAdapter(private val clickListener: UserScorecardClickListener) :
    ListAdapter<UserScorecard, UserScorecardViewHolder>(UserScorecardDiffCallback()) {

    override fun onBindViewHolder(holder: UserScorecardViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserScorecardViewHolder {
        return UserScorecardViewHolder.from(parent)
    }

}

class UserScorecardViewHolder private constructor(private val binding: UserScorecardViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        userScorecard: UserScorecard,
        clickListener: UserScorecardClickListener
    ) {
        binding.userScorecard = userScorecard
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): UserScorecardViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = UserScorecardViewBinding.inflate(layoutInflater, parent, false)
            return UserScorecardViewHolder(binding)
        }
    }
}

class UserScorecardDiffCallback : DiffUtil.ItemCallback<UserScorecard>() {
    override fun areItemsTheSame(oldItem: UserScorecard, newItem: UserScorecard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserScorecard, newItem: UserScorecard): Boolean {
        return oldItem == newItem
    }
}

class UserScorecardClickListener(val clickListener: (roundId: Int) -> Unit) {
    fun onClick(userScorecard: UserScorecard) = clickListener(userScorecard.roundId);
}