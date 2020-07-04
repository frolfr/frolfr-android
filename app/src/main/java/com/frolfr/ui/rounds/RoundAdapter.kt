package com.frolfr.ui.rounds

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.frolfr.R
import com.frolfr.api.model.Round
import com.frolfr.databinding.ViewRoundItemBinding
import com.frolfr.databinding.ViewUserScoreBinding

class RoundAdapter(private val clickListener: RoundClickListener, private val listEndListener: ListEndListener) :
    ListAdapter<Round, RoundItemViewHolder>(RoundDiffCallback()) {

    override fun onBindViewHolder(holder: RoundItemViewHolder, position: Int) {
        val round = getItem(position)
        holder.bind(round, clickListener)
        if (position == (itemCount - 1)) {
            listEndListener.onListEnd()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoundItemViewHolder {
        return RoundItemViewHolder.from(parent)
    }

}

abstract class ListEndListener {
    abstract fun onListEnd();
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
        val users = round.getUsers()
        for (user in users) {
            val userScoreBinding = ViewUserScoreBinding.inflate(childLayoutInflater, binding.layoutRoundUsers, true)
            val userId = user.id
            userScoreBinding.userId = userId.toInt()
            userScoreBinding.score = round.getUserScore(userId)
            Glide.with(binding.root.context).load(user?.avatarUrl?.toUri())
                .apply(
                    RequestOptions()
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