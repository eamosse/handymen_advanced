package com.vama.android.handymen.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.android.data.model.User
import com.vama.android.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // TODO Obeserver le livedata du repository pour éviter de faire un refresh à chaque fois
    private val _favorites = MutableLiveData<List<User>>()
    val favorites: LiveData<List<User>> = _favorites

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _favorites.value = userRepository.getFavorites()
        }
    }

    fun toggleFavorite(userId: Long) {
        viewModelScope.launch {
            userRepository.toggleFavorite(userId)
            loadFavorites()
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            userRepository.delete(userId)
            loadFavorites()
        }
    }

    fun refreshFavorites() {
        loadFavorites()
    }
}
