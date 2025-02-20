package com.vama.android.handymen.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vama.android.handymen.R
import com.vama.android.handymen.databinding.UserItemBinding
import com.vama.android.handymen.model.UserModelView

class UsersAdapter(
    private val onFavoriteClick: (UserModelView) -> Unit,
    private val onDeleteClick: (UserModelView) -> Unit
) : RecyclerView.Adapter<UserViewHolder>() {
    private val mDiffer: AsyncListDiffer<UserModelView> = AsyncListDiffer(this, DiffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: UserItemBinding = UserItemBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding, onFavoriteClick, onDeleteClick)
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
    private val onFavoriteClick: (UserModelView) -> Unit,
    private val onDeleteClick: (UserModelView) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private var currentUser: UserModelView? = null

    init {
        // Setup favorite icon click listener
        binding.favorite.setOnClickListener {
            currentUser?.let { user -> onFavoriteClick(user) }
        }

        // Setup delete icon click listener
        binding.delete.setOnClickListener {
            currentUser?.let { user -> onDeleteClick(user) }
        }
    }

    fun bind(user: UserModelView) {
        currentUser = user
        binding.name.text = user.name
        binding.address.text = user.address
        binding.phoneNumber.text = user.phoneNumber
        binding.webSite.text = user.webSite
        binding.aboutMe.text = user.aboutMe

        // Set favorite icon
        val favoriteIcon = if (user.favorite) {
            R.drawable.ic_favorite  // filled heart
        } else {
            R.drawable.ic_favorite_border // outlined heart
        }
        binding.favorite.setImageResource(favoriteIcon)
    }
}