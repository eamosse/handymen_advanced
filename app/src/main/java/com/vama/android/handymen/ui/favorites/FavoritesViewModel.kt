package com.vama.android.handymen.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vama.android.data.model.User
import com.vama.android.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _favorites = MutableLiveData<List<User>>()
    val favorites: LiveData<List<User>> = _favorites

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        _favorites.value = userRepository.getFavorites()
    }

    fun toggleFavorite(userId: Long) {
        userRepository.toggleFavorite(userId)
        loadFavorites()
    }

    fun deleteUser(userId: Long) {
        userRepository.delete(userId)
        loadFavorites()
    }

    fun refreshFavorites() {
        loadFavorites()
    }
}
