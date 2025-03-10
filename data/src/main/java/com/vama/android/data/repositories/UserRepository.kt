package com.vama.android.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vama.android.data.api.SortCriteria
import com.vama.android.data.api.UserService
import com.vama.android.data.di.DatabaseUserService
import com.vama.android.data.di.MemoryUserService
import com.vama.android.data.di.RemoteUserService
import com.vama.android.data.model.User
import com.vama.android.data.preferences.DataSource
import com.vama.android.data.preferences.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface UserRepository {
    suspend fun getAll(): LiveData<List<User>>
    suspend fun getById(id: Long): User?
    suspend fun add(user: User): User
    suspend fun update(user: User): User
    suspend fun delete(id: Long)
    suspend fun search(query: String): List<User>
    suspend fun toggleFavorite(id: Long)
    suspend fun getFavorites(): List<User>
    suspend fun sortBy(criteria: SortCriteria): List<User>

    // New methods for data source management
    fun getCurrentDataSource(): DataSource
    fun setDataSource(source: DataSource)
    fun isSyncEnabled(): Boolean
    fun setSyncEnabled(enabled: Boolean)
    suspend fun syncData()
}


class UserRepositoryImpl @Inject constructor(
    private val currentUserService: UserService,
    @MemoryUserService private val memoryUserService: UserService,
    @DatabaseUserService private val databaseUserService: UserService,
    @RemoteUserService private val onlineUserService: UserService,
    private val dataStoreManager: DataStoreManager
) : UserRepository {
    private val _users = MutableLiveData<List<User>>()
    private var lastSortCriteria: SortCriteria? = null
    private var lastSearchQuery: String? = null

    private suspend fun refreshUsers() {
        // Apply any active filters or sorting
        when {
            lastSearchQuery != null -> {
                _users.value = currentUserService.search(lastSearchQuery!!)
            }
            lastSortCriteria != null -> {
                _users.value = currentUserService.sortBy(lastSortCriteria!!)
            }
            else -> {
                _users.value = currentUserService.getAll()
            }
        }
    }

    override suspend fun getAll(): LiveData<List<User>> {
        lastSearchQuery = null
        lastSortCriteria = null
        refreshUsers()
        return _users
    }

    override suspend fun getById(id: Long): User? = currentUserService.getById(id)

    override suspend fun add(user: User): User {
        val addedUser = currentUserService.add(user)

        // If sync is enabled, add to online storage as well
        if (dataStoreManager.isSyncEnabled() && getCurrentDataSource() != DataSource.ONLINE) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    onlineUserService.add(addedUser)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        refreshUsers()
        return addedUser
    }

    override suspend fun update(user: User): User {
        val updatedUser = currentUserService.update(user)

        // If sync is enabled, update online storage as well
        if (dataStoreManager.isSyncEnabled() && getCurrentDataSource() != DataSource.ONLINE) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    onlineUserService.update(updatedUser)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        refreshUsers()
        return updatedUser
    }

    override suspend fun delete(id: Long) {
        currentUserService.delete(id)

        // If sync is enabled, delete from online storage as well
        if (dataStoreManager.isSyncEnabled() && getCurrentDataSource() != DataSource.ONLINE) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    onlineUserService.delete(id)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        refreshUsers()
    }

    override suspend fun search(query: String): List<User> {
        lastSearchQuery = query
        lastSortCriteria = null
        val users = currentUserService.search(query)
        _users.value = users
        return users
    }

    override suspend fun toggleFavorite(id: Long) {
        currentUserService.toggleFavorite(id)

        // If sync is enabled, toggle in online storage as well
        if (dataStoreManager.isSyncEnabled() && getCurrentDataSource() != DataSource.ONLINE) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    onlineUserService.toggleFavorite(id)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        refreshUsers()
    }

    override suspend fun getFavorites(): List<User> = currentUserService.getFavorites()

    override suspend fun sortBy(criteria: SortCriteria): List<User> {
        lastSortCriteria = criteria
        lastSearchQuery = null
        val users = currentUserService.sortBy(criteria)
        _users.value = users
        return users
    }

    override fun getCurrentDataSource(): DataSource {
        return dataStoreManager.getDataSource()
    }

    override fun setDataSource(source: DataSource) {
        dataStoreManager.setDataSource(source)
    }

    override fun isSyncEnabled(): Boolean {
        return dataStoreManager.isSyncEnabled()
    }

    override fun setSyncEnabled(enabled: Boolean) {
        dataStoreManager.setSyncEnabled(enabled)
    }
    suspend fun syncLocalToOnline(): Boolean = withContext(Dispatchers.IO) {
        try {
            // 1. Récupérer toutes les données de la source locale
            val localUsers = when (getCurrentDataSource()) {
                DataSource.MEMORY -> memoryUserService.getAll()
                DataSource.DATABASE -> databaseUserService.getAll()
                DataSource.ONLINE -> {
                    // Si on est déjà en mode online, on peut récupérer toutes les données
                    // mais cela n'a pas vraiment de sens de faire un syncLocalToOnline dans ce cas
                    return@withContext false
                }
            }

            // Si aucune donnée locale, rien à synchroniser
            if (localUsers.isEmpty()) {
                return@withContext true
            }

            // 2. Récupérer tous les utilisateurs distants pour pouvoir les supprimer
            val onlineUsers = onlineUserService.getAll()

            // 3. Supprimer tous les utilisateurs de la base distante
            for (onlineUser in onlineUsers) {
                onlineUserService.delete(onlineUser.id)
            }

            // 4. Ajouter tous les utilisateurs locaux à la base distante
            for (localUser in localUsers) {
                // On crée une copie de l'utilisateur sans son ID pour qu'un nouvel ID soit généré
                // car MongoDB va générer son propre ID
                val userToAdd = localUser.copy(id = 0)
                onlineUserService.add(userToAdd)
            }

            // 5. Rafraîchir l'UI si nécessaire
            if (getCurrentDataSource() == DataSource.ONLINE) {
                refreshUsers()
            }

            return@withContext true
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        }
    }
    override suspend fun syncData() {
        if (!dataStoreManager.isSyncEnabled()) {
            return
        }

        try {
            // Synchronize data based on the current source
            when (getCurrentDataSource()) {
                DataSource.MEMORY -> {
                    // Sync from online to memory
                    val onlineUsers = onlineUserService.getAll()
                    onlineUsers.forEach { user ->
                        val existingUser = memoryUserService.getById(user.id)
                        if (existingUser == null) {
                            memoryUserService.add(user)
                        } else {
                            memoryUserService.update(user)
                        }
                    }
                }
                DataSource.DATABASE -> {
                    // Sync from online to database
                    val onlineUsers = onlineUserService.getAll()
                    onlineUsers.forEach { user ->
                        val existingUser = databaseUserService.getById(user.id)
                        if (existingUser == null) {
                            databaseUserService.add(user)
                        } else {
                            databaseUserService.update(user)
                        }
                    }
                }
                DataSource.ONLINE -> {
                    // Sync from database to online
                    val databaseUsers = databaseUserService.getAll()
                    databaseUsers.forEach { user ->
                        onlineUserService.add(user)
                    }
                    // Refresh the displayed users after sync
                    refreshUsers()
                }
            }

            // Refresh the displayed users after sync
            refreshUsers()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}