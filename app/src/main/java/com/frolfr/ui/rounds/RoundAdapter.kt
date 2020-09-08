package com.frolfr.ui.rounds

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.frolfr.R
import com.frolfr.domain.model.Round
import com.frolfr.databinding.ViewRoundItemBinding
import com.frolfr.databinding.ViewUserScoreBinding

class RoundAdapter(private val clickListener: RoundClickListener) :
    ListAdapter<Round, RoundItemViewHolder>(RoundDiffCallback()) {

    override fun onBindViewHolder(holder: RoundItemViewHolder, position: Int) {
        val round = getItem(position)
        holder.bind(round, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoundItemViewHolder {
        return RoundItemViewHolder.from(parent)
    }
}

class RoundItemViewHolder private constructor(private val binding: ViewRoundItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        round: Round,
        clickListener: RoundClickListener
    ) {
        binding.round = round
        binding.clickListener = clickListener

        binding.layoutRoundUsers.removeAllViews()

        val childLayoutInflater = LayoutInflater.from(binding.root.context)
        val round = binding.round!!
        val userScorecards = round.userScorecards
        for (userScorecard in userScorecards) {
            val userScoreBinding =
                ViewUserScoreBinding.inflate(childLayoutInflater, binding.layoutRoundUsers, true)
            val user = userScorecard.user
            userScoreBinding.userId = user.id
            userScoreBinding.score = userScorecard.score
            userScoreBinding.isComplete = userScorecard.isComplete
            Glide.with(binding.root.context).load(user.avatarUri?.toUri())
                .apply(
                    RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                ).into(userScoreBinding.imageUserAvatar)
        }

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): RoundItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ViewRoundItemBinding.inflate(layoutInflater, parent, false)
            return RoundItemViewHolder(binding)
        }
    }
}

class RoundDiffCallback : DiffUtil.ItemCallback<Round>() {
    override fun areItemsTheSame(oldItem: Round, newItem: Round): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Round, newItem: Round): Boolean {
        return oldItem == newItem
    }
}

class RoundClickListener(val clickListener: (round: Round) -> Unit) {
    fun onClick(round: Round) = clickListener(round);
}