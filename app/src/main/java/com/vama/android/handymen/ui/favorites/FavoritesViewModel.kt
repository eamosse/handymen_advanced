package com.vama.android.handymen.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vama.android.data.di.DataModule
import com.vama.android.data.model.User
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.ui.home.HomeViewModel

class FavoritesViewModel(
    private val userRepository: UserRepository = DataModule.repository(),
    private val sharedViewModel: HomeViewModel
) : ViewModel() {

    private val _favorites = MutableLiveData<List<User>>()
    val favorites: LiveData<List<User>> = _favorites

    init {
        // Observe changes in the home view model's users
        sharedViewModel.users.observeForever { _ ->
            // Refresh favorites when users change
            loadFavorites()
        }

        // Initial load of favorites
        loadFavorites()
    }

    private fun loadFavorites() {
        val favs = userRepository.getFavorites()
        _favorites.value = favs
    }

    fun toggleFavorite(userId: Long) {
        userRepository.toggleFavorite(userId)
        // Reload favorites and trigger home view model update
        loadFavorites()
        sharedViewModel.loadUsers()
    }

    fun deleteUser(userId: Long) {
        userRepository.delete(userId)
        // Reload favorites and trigger home view model update
        loadFavorites()
        sharedViewModel.loadUsers()
    }

    fun refreshFavorites() {
        loadFavorites()
    }
}