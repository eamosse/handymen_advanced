package com.vama.android.handymen.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vama.android.data.model.User
import com.vama.android.handymen.R
import com.vama.android.handymen.databinding.UserItemBinding

class FavoritesAdapter(
    private val onFavoriteClick: (User) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    private var favorites: List<User> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = UserItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoriteViewHolder(binding, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    override fun getItemCount(): Int = favorites.size

    fun updateFavorites(newFavorites: List<User>) {
        favorites = newFavorites
        notifyDataSetChanged()
    }

    inner class FavoriteViewHolder(
        private val binding: UserItemBinding,
        private val onFavoriteClick: (User) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
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

            // Setup favorite icon click listener
            binding.favorite.setOnClickListener {
                onFavoriteClick(user)
            }
        }
    }
}