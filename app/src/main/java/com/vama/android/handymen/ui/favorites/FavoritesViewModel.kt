package com.vama.android.handymen.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vama.android.data.di.DataModule
import com.vama.android.data.model.User
import com.vama.android.data.repositories.UserRepository

class FavoritesViewModel : ViewModel() {
    private val userRepository: UserRepository = DataModule.repository()

    private val _favorites = MutableLiveData<List<User>>()
    val favorites: LiveData<List<User>> = _favorites

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        val favs = userRepository.getFavorites()
        println("DEBUG: Loaded favorites: ${favs.size}")
        _favorites.value = favs
    }

    fun toggleFavorite(userId: Long) {
        userRepository.toggleFavorite(userId)
        loadFavorites() // Reload favorites after toggle
    }
}