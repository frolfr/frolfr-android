package com.frolfr.ui.course.tab.userCourseScorecards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.frolfr.api.model.UserScorecardSummary
import com.frolfr.databinding.UserScorecardViewBinding

class UserScorecardAdapter(private val clickListener: UserScorecardClickListener) :
    ListAdapter<UserScorecardSummary, UserScorecardViewHolder>(UserScorecardDiffCallback()) {

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
        userScorecardSummary: UserScorecardSummary,
        clickListener: UserScorecardClickListener
    ) {
        binding.userScorecard = userScorecardSummary
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

class UserScorecardDiffCallback : DiffUtil.ItemCallback<UserScorecardSummary>() {
    override fun areItemsTheSame(oldItem: UserScorecardSummary, newItem: UserScorecardSummary): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserScorecardSummary, newItem: UserScorecardSummary): Boolean {
        return oldItem == newItem
    }
}

class UserScorecardClickListener(val clickListener: (roundId: Int) -> Unit) {
    fun onClick(userScorecardSummary: UserScorecardSummary) = clickListener(userScorecardSummary.roundId);
}