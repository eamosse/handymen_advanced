package com.vama.android.handymen.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.domain.UsersUseCase
import com.vama.android.handymen.model.UserModelView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val usersUseCase: UsersUseCase,
    private val userRepository: UserRepository
) : ViewModel() {
    enum class SortType {
        NAME_ASC,
        NAME_DESC,
        DATE_CREATED_ASC,
        DATE_CREATED_DESC
    }

    private val _users = MutableLiveData<List<UserModelView>>()
    private val _searchQuery = MutableLiveData("")
    private val _sortType = MutableLiveData(SortType.NAME_ASC)
    private val _filteredUsers = MediatorLiveData<List<UserModelView>>()
    val filteredUsers: LiveData<List<UserModelView>> = _filteredUsers

    init {
        Log.d("HomeViewModel", "Initialisation")
        setupFilteredUsers()
        loadUsers() // Chargement initial
    }

    private fun setupFilteredUsers() {
        _filteredUsers.addSource(_users) { performFilterAndSort() }
        _filteredUsers.addSource(_searchQuery) { performFilterAndSort() }
        _filteredUsers.addSource(_sortType) { performFilterAndSort() }
    }

    fun loadUsers() {
        viewModelScope.launch {
            Log.d("HomeViewModel", "Chargement des utilisateurs depuis le repository")
            try {
                // Récupérer les données sur un thread d'arrière-plan
                val result = withContext(Dispatchers.IO) {
                    usersUseCase()
                }

                // Mais mettre à jour la LiveData sur le thread principal
                withContext(Dispatchers.Main) {
                    result.value?.let { newUsers ->
                        Log.d("HomeViewModel", "Données récupérées: ${newUsers.size} utilisateurs")
                        _users.value = ArrayList(newUsers) // Forcer une nouvelle instance
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Erreur lors du chargement des utilisateurs", e)
            }
        }
    }

    fun toggleFavorite(userId: Long) {
        Log.d("HomeViewModel", "Demande de basculement de favori pour l'utilisateur: $userId")
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    userRepository.toggleFavorite(userId)
                }

                // Recharger immédiatement les données après avoir basculé le favori
                loadUsers()

                Log.d("HomeViewModel", "Opération de basculement de favori terminée avec succès")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Erreur lors du basculement de favori", e)

                // Même en cas d'erreur, essayer de rafraîchir les données
                loadUsers()
            }
        }
    }

    fun deleteUser(userId: Long) {
        Log.d("HomeViewModel", "Demande de suppression pour l'utilisateur: $userId")
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    userRepository.delete(userId)
                }

                // Recharger immédiatement après la suppression
                loadUsers()

                Log.d("HomeViewModel", "Suppression réussie pour l'utilisateur: $userId")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Erreur lors de la suppression de l'utilisateur", e)

                // Recharger quand même pour s'assurer que l'UI est à jour
                loadUsers()
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSortType(sortType: SortType) {
        _sortType.value = sortType
    }

    private fun performFilterAndSort() {
        val currentUsers = _users.value ?: return
        val currentQuery = _searchQuery.value ?: ""
        val currentSortType = _sortType.value ?: SortType.NAME_ASC

        Log.d("HomeViewModel", "Application des filtres et du tri")

        // Filtrer les utilisateurs selon la requête
        val filteredList = currentUsers.filter { user ->
            currentQuery.isEmpty() ||
                    user.name.contains(currentQuery, ignoreCase = true) ||
                    user.phoneNumber.contains(currentQuery, ignoreCase = true) ||
                    user.address.contains(currentQuery, ignoreCase = true)
        }

        // Trier les utilisateurs selon le critère sélectionné
        val sortedList = when (currentSortType) {
            SortType.NAME_ASC -> filteredList.sortedBy { it.name }
            SortType.NAME_DESC -> filteredList.sortedByDescending { it.name }
            SortType.DATE_CREATED_ASC -> filteredList.sortedBy { it.id }
            SortType.DATE_CREATED_DESC -> filteredList.sortedByDescending { it.id }
        }

        // Mettre à jour la liste filtrée
        _filteredUsers.value = ArrayList(sortedList) // Forcer une nouvelle instance
    }

    fun shareUserProfile(user: UserModelView): String {
        return """
            Profil Utilisateur:
            Nom: ${user.name}
            Adresse: ${user.address}
            Téléphone: ${user.phoneNumber}
            Site Web: ${user.webSite}
            À propos: ${user.aboutMe}
        """.trimIndent()
    }
}