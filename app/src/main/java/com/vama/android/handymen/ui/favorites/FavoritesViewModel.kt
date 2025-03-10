package com.vama.android.handymen.ui.favorites

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.android.data.model.User
import com.vama.android.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _favorites = MutableLiveData<List<User>>()
    val favorites: LiveData<List<User>> = _favorites

    init {
        Log.d("FavoritesViewModel", "Initialisation et chargement des favoris")
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            try {
                Log.d("FavoritesViewModel", "Chargement des favoris depuis le repository")
                // S'assurer que les données sont chargées sur un thread background
                val favoritesList = withContext(Dispatchers.IO) {
                    userRepository.getFavorites()
                }

                // Mais mettre à jour la LiveData sur le thread principal
                withContext(Dispatchers.Main) {
                    Log.d("FavoritesViewModel", "Favoris chargés: ${favoritesList.size} utilisateurs")
                    _favorites.value = favoritesList
                }
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "Erreur lors du chargement des favoris", e)
                // En cas d'erreur, au moins essayer de mettre à jour l'UI avec une liste vide
                _favorites.value = emptyList()
            }
        }
    }

    fun toggleFavorite(userId: Long) {
        viewModelScope.launch {
            try {
                Log.d("FavoritesViewModel", "Basculement du favori pour l'utilisateur: $userId")
                withContext(Dispatchers.IO) {
                    userRepository.toggleFavorite(userId)
                }

                // Recharger immédiatement la liste des favoris après le basculement
                loadFavorites()

                Log.d("FavoritesViewModel", "Favori basculé avec succès")
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "Erreur lors du basculement du favori", e)
                // Recharger quand même pour s'assurer que l'UI est à jour
                loadFavorites()
            }
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            try {
                Log.d("FavoritesViewModel", "Suppression de l'utilisateur: $userId")
                withContext(Dispatchers.IO) {
                    userRepository.delete(userId)
                }

                // Recharger immédiatement la liste des favoris après la suppression
                loadFavorites()

                Log.d("FavoritesViewModel", "Utilisateur supprimé avec succès")
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "Erreur lors de la suppression de l'utilisateur", e)
                // Recharger quand même pour s'assurer que l'UI est à jour
                loadFavorites()
            }
        }
    }

    fun refreshFavorites() {
        Log.d("FavoritesViewModel", "Rafraîchissement des favoris demandé")
        loadFavorites()
    }
}