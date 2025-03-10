package com.vama.android.handymen.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vama.android.handymen.R
import com.vama.android.handymen.databinding.UserItemBinding
import com.vama.android.handymen.model.UserModelView
import coil.load
import coil.transform.CircleCropTransformation

class UsersAdapter(
    private val listener: UserInteractionListener
) : RecyclerView.Adapter<UserViewHolder>() {
    private val mDiffer: AsyncListDiffer<UserModelView> = AsyncListDiffer(this, DiffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: UserItemBinding = UserItemBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitList(it: List<UserModelView>) {
        return mDiffer.submitList(it)
    }

    private object DiffCallback : DiffUtil.ItemCallback<UserModelView>() {
        override fun areItemsTheSame(
            oldItem: UserModelView,
            newItem: UserModelView
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UserModelView,
            newItem: UserModelView
        ): Boolean {
            return oldItem == newItem
        }
    }
}

class UserViewHolder(
    private val binding: UserItemBinding,
    private val listener: UserInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    private var currentUser: UserModelView? = null

    init {
        // Setup favorite icon click listener
        binding.favorite.setOnClickListener {
            currentUser?.let { user -> listener.onFavoriteClick(user) }
        }

        // Setup delete icon click listener
        binding.delete.setOnClickListener {
            currentUser?.let { user -> listener.onDeleteClick(user) }
        }

        // Setup share icon click listener
        binding.root.findViewById<ImageView>(R.id.share).setOnClickListener {
            currentUser?.let { user -> listener.onShareClick(user) }
        }

        // Setup item click listener for navigation to detail
        binding.root.setOnClickListener {
            currentUser?.let { user -> listener.onUserClick(user) }
        }
    }

    fun bind(user: UserModelView) {
        currentUser = user
        binding.name.text = user.name
        binding.address.text = user.address
        binding.phoneNumber.text = user.phoneNumber
        binding.webSite.text = user.webSite
        binding.aboutMe.text = user.aboutMe

        // ajoute "?seed=${user.name}" à l'URL de l'avatar
        val avatarUrl = "${user.avatarUrl}?seed=${user.name}"
        binding.imageProfile.load(avatarUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
            placeholder(R.drawable.ic_profile_placeholder)
            error(R.drawable.ic_profile_placeholder)
        }

        // Set favorite icon
        val favoriteIcon = if (user.favorite) {
            R.drawable.ic_favorite  // filled heart
        } else {
            R.drawable.ic_favorite_border // outlined heart
        }
        binding.favorite.setImageResource(favoriteIcon)
    }
}