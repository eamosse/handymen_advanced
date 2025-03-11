package com.vama.android.handymen.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.android.data.model.Identity
import com.vama.android.data.utils.DataResult
import com.vama.android.handymen.domain.DeleteUserUseCase
import com.vama.android.handymen.domain.RefreshUseCase
import com.vama.android.handymen.domain.ToggleFavoriteUseCase
import com.vama.android.handymen.domain.UsersUseCase
import com.vama.android.handymen.domain.mapSuccess
import com.vama.android.handymen.model.UserModelView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val usersUseCase: UsersUseCase,
    private val refreshUseCase: RefreshUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,

    ) : ViewModel() {
    enum class SortType {
        NAME_ASC,
        NAME_DESC,
        DATE_CREATED_ASC,
        DATE_CREATED_DESC
    }

    private val _users = MediatorLiveData<DataResult<List<UserModelView>>>()
    private val _searchQuery = MutableLiveData("")
    private val _sortType = MutableLiveData(SortType.NAME_ASC)
    private val _filteredUsers = MediatorLiveData<DataResult<List<UserModelView>>>()
    val filteredUsers: LiveData<DataResult<List<UserModelView>>> = _filteredUsers

    init {
        setupFilteredUsers()
        loadUsers()

    }

    private fun setupFilteredUsers() = viewModelScope.launch {
        _users.addSource(usersUseCase()) {
            _users.value = it
        }
        _filteredUsers.addSource(_users) { performFilterAndSort() }
        _filteredUsers.addSource(_searchQuery) { performFilterAndSort() }
        _filteredUsers.addSource(_sortType) { performFilterAndSort() }
    }

    fun loadUsers() = viewModelScope.launch {
        refreshUseCase()
    }

    fun toggleFavorite(userId: Identity, toogle: Boolean) = viewModelScope.launch {
        toggleFavoriteUseCase(userId, toogle)
    }

    fun deleteUser(userId: Identity) = viewModelScope.launch {
        deleteUserUseCase(userId)
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

        // TODO: Move this in the repository
        _filteredUsers.value = currentUsers.mapSuccess { result ->
            val filteredList = result.filter { user ->
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
            sortedList.toList()
        }

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