package com.vama.android.handymen.ui.home

import android.util.Log
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

// TODO Utiliser une interface de préférence
class UsersAdapter(
    private val onFavoriteClick: (UserModelView) -> Unit,
    private val onDeleteClick: (UserModelView) -> Unit,
    private val onShareClick: (UserModelView) -> Unit
) : RecyclerView.Adapter<UserViewHolder>() {
    private val mDiffer: AsyncListDiffer<UserModelView> = AsyncListDiffer(this, DiffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: UserItemBinding = UserItemBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding, onFavoriteClick, onDeleteClick, onShareClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitList(newList: List<UserModelView>) {
        Log.d("UsersAdapter", "Soumission d'une nouvelle liste de ${newList.size} utilisateurs")
        mDiffer.submitList(ArrayList(newList)) // Créer une nouvelle liste pour forcer la détection de changements
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
            // S'assurer que le changement de statut favori est détecté
            return oldItem == newItem && oldItem.favorite == newItem.favorite
        }
    }
}

class UserViewHolder(
    private val binding: UserItemBinding,
    private val onFavoriteClick: (UserModelView) -> Unit,
    private val onDeleteClick: (UserModelView) -> Unit,
    private val onShareClick: (UserModelView) -> Unit
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

        // Ajout de l'icône de partage
        val shareIcon = binding.root.findViewById<ImageView>(R.id.share)
        shareIcon.setOnClickListener {
            currentUser?.let { user -> onShareClick(user) }
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