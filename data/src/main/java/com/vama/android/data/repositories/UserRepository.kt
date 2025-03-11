package com.vama.android.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vama.android.data.model.SortCriteria
import com.vama.android.data.services.UserService
import com.vama.android.data.di.DatabaseUserService
import com.vama.android.data.di.MemoryUserService
import com.vama.android.data.di.RemoteUserService
import com.vama.android.data.model.AddUser
import com.vama.android.data.model.Identity
import com.vama.android.data.model.User
import com.vama.android.data.utils.DataSource
import com.vama.android.data.preferences.DataStoreManager
import com.vama.android.data.utils.DataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface UserRepository {
    val users: LiveData<DataResult<List<User>>>
    suspend fun getAll()
    suspend fun getById(id: Identity): DataResult<User>
    suspend fun add(user: AddUser): DataResult<User>
    suspend fun update(user: User): DataResult<User>
    suspend fun delete(id: Identity): DataResult<Unit>
    suspend fun search(query: String): DataResult<List<User>>
    suspend fun toggleFavorite(id: Identity, toggle: Boolean): DataResult<Unit>
    suspend fun getFavorites(): DataResult<List<User>>
    suspend fun sortBy(criteria: SortCriteria): DataResult<List<User>>
    fun getCurrentDataSource(): DataSource
    fun setDataSource(source: DataSource)
    fun isSyncEnabled(): Boolean
    fun setSyncEnabled(enabled: Boolean)
    suspend fun syncData()
}


class UserRepositoryImpl @Inject constructor(
    private val currentUserService: UserService,
    @DatabaseUserService private val databaseUserService: UserService,
    @RemoteUserService private val onlineUserService: UserService,
    private val dataStoreManager: DataStoreManager
) : UserRepository {
    private val _users = MutableLiveData<DataResult<List<User>>>()
    override val users: LiveData<DataResult<List<User>>>
        get() = _users

    private var lastSortCriteria: SortCriteria? = null
    private var lastSearchQuery: String? = null

    private suspend fun refreshUsers() {
        Log.d("UserRepository", "Rafraîchissement des utilisateurs en cours...")
        val users = when {
            lastSearchQuery != null -> {
                Log.d("UserRepository", "Applique le filtre de recherche: $lastSearchQuery")
                currentUserService.search(lastSearchQuery!!)
            }

            lastSortCriteria != null -> {
                Log.d("UserRepository", "Applique le tri: $lastSortCriteria")
                currentUserService.sortBy(lastSortCriteria!!)
            }

            else -> {
                Log.d("UserRepository", "Récupère tous les utilisateurs")
                currentUserService.getAll()
            }
        }

        // Important: Mettre à jour la valeur sur le thread principal pour notifier les observateurs
        withContext(Dispatchers.Main) {
            _users.value = users
        }
    }

    override suspend fun getAll() {
        lastSearchQuery = null
        lastSortCriteria = null
        refreshUsers()
    }

    override suspend fun getById(id: Identity): DataResult<User> = currentUserService.getById(id)

    override suspend fun add(user: AddUser): DataResult<User> {
        val addedUser = currentUserService.add(user)

        // If sync is enabled, add to online storage as well
        if (dataStoreManager.isSyncEnabled() && getCurrentDataSource() != DataSource.ONLINE) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    onlineUserService.add(user)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        refreshUsers()
        return addedUser
    }

    override suspend fun update(user: User): DataResult<User> {
        val updatedUser = currentUserService.update(user)

        // If sync is enabled, update online storage as well
        if (dataStoreManager.isSyncEnabled() && getCurrentDataSource() != DataSource.ONLINE) {
            CoroutineScope(Dispatchers.IO).launch {
                onlineUserService.update(user)
            }
        }
        refreshUsers()
        return updatedUser
    }

    override suspend fun delete(id: Identity): DataResult<Unit> {
        Log.d("UserRepository", "Suppression de l'utilisateur avec ID: $id")
        currentUserService.delete(id)
        if (dataStoreManager.isSyncEnabled() && getCurrentDataSource() != DataSource.ONLINE) {
            return onlineUserService.delete(id)
        }
        refreshUsers()
        return DataResult.Success(Unit)
    }

    override suspend fun search(query: String): DataResult<List<User>> {
        lastSearchQuery = query
        lastSortCriteria = null
        val users = currentUserService.search(query)
        _users.value = users
        return users
    }


    override suspend fun toggleFavorite(id: Identity, toggle: Boolean): DataResult<Unit> {
        Log.d("UserRepository", "Changement du statut favori pour l'utilisateur avec ID: $id")
        currentUserService.toggleFavorite(id, toggle)
        if (dataStoreManager.isSyncEnabled() && getCurrentDataSource() != DataSource.ONLINE) {
            // TODO handle error
            onlineUserService.toggleFavorite(id, toggle)
        }
        refreshUsers()
        return DataResult.Success(Unit)
    }

    override suspend fun getFavorites(): DataResult<List<User>> = currentUserService.getFavorites()

    override suspend fun sortBy(criteria: SortCriteria): DataResult<List<User>> {
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

    override suspend fun syncData() {
        Log.d("UserRepository", "Démarrage de la synchronisation")
        if (!dataStoreManager.isSyncEnabled()) {
            Log.d("UserRepository", "Synchronisation désactivée")
            return
        }
        syncLocalToOnline()
        refreshUsers()
    }

    suspend fun <T : Any> DataResult<T>.doOnSuccess(action: suspend (T) -> Unit): DataResult<T> {
        if (this is DataResult.Success) {
            action(data)
        }
        return this
    }


    private suspend fun syncLocalToOnline(): Boolean = withContext(Dispatchers.IO) {
        Log.d("UserRepository", "Début de syncLocalToOnline")
        val localUsers = when (getCurrentDataSource()) {
            DataSource.DATABASE -> {
                Log.d("UserRepository", "Récupération des utilisateurs depuis DATABASE")
                databaseUserService.getAll()
            }

            DataSource.ONLINE -> {
                Log.d("UserRepository", "Déjà en mode ONLINE, rien à synchroniser")
                return@withContext true
            }
        }

        localUsers.doOnSuccess {
            Log.d("UserRepository", "Récupération de ${it.size} utilisateurs locaux")
            onlineUserService.sync(it)
        }
        return@withContext true
    }
}