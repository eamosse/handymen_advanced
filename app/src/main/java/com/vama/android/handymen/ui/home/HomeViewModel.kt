package com.vama.android.handymen.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.android.data.di.DataModule
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.domain.UsersUseCase
import com.vama.android.handymen.model.UserModelView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    // TODO Ces variables sont inutiles
    private val _users = MutableLiveData<List<UserModelView>>()
    private val _searchQuery = MutableLiveData("")
    private val _sortType = MutableLiveData(SortType.NAME_ASC)
    private val _filteredUsers = MediatorLiveData<List<UserModelView>>()
    val filteredUsers: LiveData<List<UserModelView>> = _filteredUsers

    init {
        loadUsers()
        setupFilteredUsers()
    }

    private fun setupFilteredUsers() {
        _filteredUsers.addSource(_users) { performFilterAndSort() }
        _filteredUsers.addSource(_searchQuery) { performFilterAndSort() }
        _filteredUsers.addSource(_sortType) { performFilterAndSort() }
    }

    fun loadUsers() {
        viewModelScope.launch {
            usersUseCase().value?.let { newUsers ->
                _users.value = newUsers
            }
        }
    }

    fun toggleFavorite(userId: Long) {
        viewModelScope.launch {
            userRepository.toggleFavorite(userId)
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            try {
                // Supprimer l'utilisateur et attendre que l'opération soit terminée
                userRepository.delete(userId)

                // Recharger la liste des utilisateurs après la suppression
                loadUsers()
            } catch (e: Exception) {
                // Gérer l'erreur si nécessaire
                e.printStackTrace()

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

    // TODO : Les sources de données devront appliquer les filtres
    private fun performFilterAndSort() {
        val currentUsers = _users.value ?: return
        val currentQuery = _searchQuery.value ?: ""
        val currentSortType = _sortType.value ?: SortType.NAME_ASC

        val filteredList = currentUsers.filter { user ->
            currentQuery.isEmpty() ||
                    user.name.contains(currentQuery, ignoreCase = true) ||
                    user.phoneNumber.contains(currentQuery, ignoreCase = true) ||
                    user.address.contains(currentQuery, ignoreCase = true)
        }

        val sortedList = when (currentSortType) {
            SortType.NAME_ASC -> filteredList.sortedBy { it.name }
            SortType.NAME_DESC -> filteredList.sortedByDescending { it.name }
            SortType.DATE_CREATED_ASC -> filteredList.sortedBy { it.id }
            SortType.DATE_CREATED_DESC -> filteredList.sortedByDescending { it.id }
        }

        _filteredUsers.value = sortedList
    }

    // TODO : Le text doit être traduit en fonction de la langue du téléphone
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