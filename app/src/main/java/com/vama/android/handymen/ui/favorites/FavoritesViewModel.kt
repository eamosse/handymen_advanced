package com.vama.android.handymen.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.android.data.model.Identity
import com.vama.android.data.utils.DataResult
import com.vama.android.handymen.domain.DeleteUserUseCase
import com.vama.android.handymen.domain.FavoritesUseCase
import com.vama.android.handymen.domain.ToggleFavoriteUseCase
import com.vama.android.handymen.model.UserModelView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesUseCase: FavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {

    private val _favorites = MutableLiveData<DataResult<List<UserModelView>>>()
    val favorites: LiveData<DataResult<List<UserModelView>>> = _favorites

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            val users = favoritesUseCase()
            _favorites.value = users
        }
    }

    fun toggleFavorite(userId: Identity) {
        viewModelScope.launch {
            // it the user is in this view, it means it is a favorite
            toggleFavoriteUseCase(userId, false)
            loadFavorites()
        }
    }

    fun deleteUser(userId: Identity) {
        viewModelScope.launch {
            deleteUserUseCase(userId)
            loadFavorites()
        }
    }

    fun refreshFavorites() {
        loadFavorites()
    }
}