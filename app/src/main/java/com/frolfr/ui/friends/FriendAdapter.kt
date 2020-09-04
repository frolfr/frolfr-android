package com.frolfr.ui.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.frolfr.R
import com.frolfr.databinding.ViewFriendItemBinding
import com.frolfr.domain.model.User

class FriendAdapter(private val clickListener: FriendClickListener) :
    ListAdapter<User, FriendItemViewHolder>(UserDiffCallback()) {

    override fun onBindViewHolder(holder: FriendItemViewHolder, position: Int) {
        val friend = getItem(position)
        holder.bind(friend, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendItemViewHolder {
        return FriendItemViewHolder.from(parent)
    }
}

class FriendItemViewHolder private constructor(private val binding: ViewFriendItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        friend: User,
        clickListener: FriendClickListener
    ) {
        binding.friend = friend
        binding.clickListener = clickListener

        Glide.with(binding.root.context).load(friend.avatarUri?.toUri())
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .circleCrop()
                    .error(R.drawable.ic_broken_image)
            ).into(binding.imageFriendAvatar)

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): FriendItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ViewFriendItemBinding.inflate(layoutInflater, parent, false)
            return FriendItemViewHolder(binding)
        }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

class FriendClickListener(val clickListener: (friend: User) -> Unit) {
    fun onClick(friend: User) = clickListener(friend);
}