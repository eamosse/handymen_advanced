package com.vama.android.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.vama.android.data.api.SortCriteria
import com.vama.android.data.api.UserService
import com.vama.android.data.di.Database
import com.vama.android.data.di.InMemory
import com.vama.android.data.model.User
import com.vama.android.data.preferences.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {
    suspend fun getAll(): LiveData<List<User>>
    suspend fun getById(id: Long): User?
    suspend fun add(user: User): User
    suspend fun update(user: User): User
    suspend fun delete(id: Long)
    suspend fun search(query: String): List<User>
    suspend fun toggleFavorite(id: Long)
    suspend fun getFavorites(): List<User>
    fun getFavoritesLiveData(): LiveData<List<User>>
    suspend fun sortBy(criteria: SortCriteria): List<User>

    suspend fun switchToDatabaseMode()
    suspend fun switchToInMemoryMode()
    fun getCurrentMode(): Boolean
}

@Singleton
class UserRepositoryImpl @Inject constructor(
    @Database private val roomUserService: UserService,
    @InMemory private val inMemoryUserService: UserService,
    private val dataStoreManager: DataStoreManager
) : UserRepository {
    private val _users = MutableLiveData<List<User>>()
    private val _favorites = MediatorLiveData<List<User>>()

    private var currentSearchQuery: String? = null
    private var currentSortCriteria: SortCriteria? = null
    private var showingFavorites: Boolean = false

    private var currentUserService: UserService

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    init {
        val useDatabaseMode = runBlocking { dataStoreManager.isDatabaseModeFlow().first() }
        currentUserService = if (useDatabaseMode) roomUserService else inMemoryUserService

        _favorites.addSource(_users) { users ->
            _favorites.value = users.filter { it.favorite }
        }

        repositoryScope.launch {
            refreshUsers()
        }
    }

    private suspend fun refreshUsers() {
        var users = currentUserService.getAll()

        currentSearchQuery?.let { query ->
            if (query.isNotEmpty()) {
                users = currentUserService.search(query)
            }
        }

        if (showingFavorites) {
            users = currentUserService.getFavorites()
        }

        currentSortCriteria?.let { criteria ->
            users = currentUserService.sortBy(criteria)
        }

        _users.postValue(users)
    }

    override suspend fun getAll(): LiveData<List<User>> {
        resetFiltersAndSorting()
        refreshUsers()
        return _users
    }

    override suspend fun getById(id: Long): User? = currentUserService.getById(id)

    override suspend fun add(user: User): User {
        val addedUser = currentUserService.add(user)
        refreshUsers()
        return addedUser
    }

    override suspend fun update(user: User): User {
        val updatedUser = currentUserService.update(user)
        refreshUsers()
        return updatedUser
    }

    override suspend fun delete(id: Long) {
        currentUserService.delete(id)
        refreshUsers()
    }

    override suspend fun search(query: String): List<User> {
        currentSearchQuery = query
        showingFavorites = false

        val users = currentUserService.search(query)

        val sortedUsers = if (currentSortCriteria != null) {
            currentUserService.sortBy(currentSortCriteria!!)
        } else {
            users
        }

        _users.value = sortedUsers
        return sortedUsers
    }

    override suspend fun toggleFavorite(id: Long) {
        currentUserService.toggleFavorite(id)
        refreshUsers()
    }

    override suspend fun getFavorites(): List<User> {
        showingFavorites = true
        currentSearchQuery = null

        val favorites = currentUserService.getFavorites()

        val sortedFavorites = if (currentSortCriteria != null) {
            val allSorted = currentUserService.sortBy(currentSortCriteria!!)
            allSorted.filter { user -> favorites.any { it.id == user.id } }
        } else {
            favorites
        }

        _users.value = sortedFavorites
        return sortedFavorites
    }

    override fun getFavoritesLiveData(): LiveData<List<User>> {
        showingFavorites = true
        currentSearchQuery = null

        repositoryScope.launch {
            refreshUsers()
        }

        return _favorites
    }

    override suspend fun sortBy(criteria: SortCriteria): List<User> {
        currentSortCriteria = criteria

        val sortedUsers = currentUserService.sortBy(criteria)

        val filteredAndSortedUsers = when {
            showingFavorites -> {
                val favorites = currentUserService.getFavorites()
                sortedUsers.filter { user -> favorites.any { it.id == user.id } }
            }
            currentSearchQuery != null && currentSearchQuery!!.isNotEmpty() -> {
                val searchResults = currentUserService.search(currentSearchQuery!!)
                sortedUsers.filter { user -> searchResults.any { it.id == user.id } }
            }
            else -> sortedUsers
        }

        _users.value = filteredAndSortedUsers
        return filteredAndSortedUsers
    }

    override suspend fun switchToDatabaseMode() {
        if (currentUserService != roomUserService) {
            val currentState = saveCurrentState()

            currentUserService = roomUserService

            dataStoreManager.setDatabaseMode(true)

            restoreState(currentState)

            refreshUsers()
        }
    }

    override suspend fun switchToInMemoryMode() {
        if (currentUserService != inMemoryUserService) {
            val currentState = saveCurrentState()

            currentUserService = inMemoryUserService

            dataStoreManager.setDatabaseMode(false)

            restoreState(currentState)

            refreshUsers()
        }
    }

    override fun getCurrentMode(): Boolean {
        return currentUserService == roomUserService
    }

    private fun resetFiltersAndSorting() {
        currentSearchQuery = null
        currentSortCriteria = null
        showingFavorites = false
    }

    private data class RepositoryState(
        val searchQuery: String?,
        val sortCriteria: SortCriteria?,
        val showingFavorites: Boolean
    )

    private fun saveCurrentState(): RepositoryState {
        return RepositoryState(
            searchQuery = currentSearchQuery,
            sortCriteria = currentSortCriteria,
            showingFavorites = showingFavorites
        )
    }

    private fun restoreState(state: RepositoryState) {
        currentSearchQuery = state.searchQuery
        currentSortCriteria = state.sortCriteria
        showingFavorites = state.showingFavorites
    }
}