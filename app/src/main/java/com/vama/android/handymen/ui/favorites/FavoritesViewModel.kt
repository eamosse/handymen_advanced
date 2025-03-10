package com.vama.android.handymen.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.android.handymen.domain.FavoritesUseCase
import com.vama.android.handymen.model.UserModelView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesUseCase: FavoritesUseCase
) : ViewModel() {

    private val _favorites = MutableLiveData<List<UserModelView>>()
    val favorites: LiveData<List<UserModelView>> = _favorites

    /**
     * Charge les utilisateurs favoris à partir du use case
     */
    fun loadFavorites() {
        viewModelScope.launch {
            _favorites.value = favoritesUseCase.getFavorites()
        }
    }

    /**
     * Bascule l'état favori d'un utilisateur
     */
    fun toggleFavorite(userId: Long) {
        viewModelScope.launch {
            favoritesUseCase.toggleFavorite(userId)
            loadFavorites() // Recharger après modification
        }
    }

    /**
     * Supprime un utilisateur
     */
    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            favoritesUseCase.deleteUser(userId)
            loadFavorites() // Recharger après suppression
        }
    }

    /**
     * Rafraîchit la liste des favoris (pour compatibilité avec l'existant)
     */
    fun refreshFavorites() {
        loadFavorites()
    }
}