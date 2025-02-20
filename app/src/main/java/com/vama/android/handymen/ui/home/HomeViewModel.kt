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
import com.vama.android.data.repositories.UserRepository
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
    private val _users = MutableLiveData<List<UserModelView>>()
    val users: LiveData<List<UserModelView>> = _users

    // Change MutableStateFlow to MutableLiveData
    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    // Change MutableStateFlow to MutableLiveData
    private val _sortType = MutableLiveData(SortType.NAME_ASC)
    val sortType: LiveData<SortType> = _sortType

    private val _filteredUsers = MediatorLiveData<List<UserModelView>>().apply {
        addSource(_users) { performFilterAndSort() }
        addSource(_searchQuery) { performFilterAndSort() }
        addSource(_sortType) { performFilterAndSort() }
    }
    val filteredUsers: LiveData<List<UserModelView>> = _filteredUsers

    // Rest of the code remains the same...

    private fun performFilterAndSort() {
        val currentUsers = _users.value ?: return
        val currentQuery = _searchQuery.value ?: ""
        val currentSortType = _sortType.value ?: SortType.NAME_ASC

        // Filtrer
        val filteredList = currentUsers.filter { user ->
            currentQuery.isEmpty() ||
                    user.name.contains(currentQuery, ignoreCase = true) ||
                    user.phoneNumber.contains(currentQuery, ignoreCase = true) ||
                    user.address.contains(currentQuery, ignoreCase = true)
        }

        // Trier
        val sortedList = when (currentSortType) {
            SortType.NAME_ASC -> filteredList.sortedBy { it.name }
            SortType.NAME_DESC -> filteredList.sortedByDescending { it.name }
            SortType.DATE_CREATED_ASC -> filteredList.sortedBy { it.id }
            SortType.DATE_CREATED_DESC -> filteredList.sortedByDescending { it.id }
        }

        _filteredUsers.value = sortedList
    }

    // Update methods to use LiveData
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSortType(sortType: SortType) {
        _sortType.value = sortType
    }
    init {
        loadUsers()
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
            loadUsers() // Reload to reflect changes
        }
    }
    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            userRepository.delete(userId)
            loadUsers() // Reload to reflect changes
        }
    }

    // Méthode de partage de profil
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