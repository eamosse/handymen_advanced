package com.vama.android.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vama.android.data.api.SortCriteria
import com.vama.android.data.api.UserService
import com.vama.android.data.api.online.UserRequest
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
            Log.d("UserRepository", "Mise à jour de la LiveData avec ${users.size} utilisateurs")
            _users.value = users
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
        Log.d("UserRepository", "Suppression de l'utilisateur avec ID: $id")

        try {
            // Supprimer l'utilisateur du service actuel
            currentUserService.delete(id)
            Log.d("UserRepository", "Utilisateur supprimé du service actuel")

            // Si la synchronisation est activée, supprimer également de l'online storage
            if (dataStoreManager.isSyncEnabled() && getCurrentDataSource() != DataSource.ONLINE) {
                try {
                    Log.d("UserRepository", "Tentative de suppression sur le serveur...")
                    onlineUserService.delete(id)
                    Log.d("UserRepository", "Utilisateur supprimé du serveur")
                } catch (e: Exception) {
                    Log.e("UserRepository", "Erreur lors de la suppression sur le serveur", e)
                    // Ne pas propager cette erreur, car la suppression locale a réussi
                }
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Erreur lors de la suppression", e)
            throw e
        } finally {
            // Importante correction : Assurez-vous de rafraîchir l'interface utilisateur
            // après la suppression, que celle-ci ait réussi ou échoué
            refreshUsers()
            Log.d("UserRepository", "Interface utilisateur rafraîchie après suppression")
        }
    }

    override suspend fun search(query: String): List<User> {
        lastSearchQuery = query
        lastSortCriteria = null
        val users = currentUserService.search(query)
        _users.value = users
        return users
    }

    // Dans UserRepositoryImpl, améliorer la méthode toggleFavorite

    // Dans UserRepositoryImpl, méthode toggleFavorite corrigée

    override suspend fun toggleFavorite(id: Long) {
        Log.d("UserRepository", "Changement du statut favori pour l'utilisateur avec ID: $id")

        try {
            // Obtenir l'utilisateur avant la modification
            val user = currentUserService.getById(id)

            if (user == null) {
                Log.e("UserRepository", "Utilisateur non trouvé avec ID: $id")
                return
            }

            Log.d("UserRepository", "Utilisateur trouvé, statut favori actuel: ${user.favorite}")

            // Changer le statut favori dans le service actuel
            currentUserService.toggleFavorite(id)
            Log.d("UserRepository", "Statut favori changé dans le service actuel")

            // Si la synchronisation est activée, changer également sur le serveur
            if (dataStoreManager.isSyncEnabled() && getCurrentDataSource() != DataSource.ONLINE) {
                try {
                    Log.d("UserRepository", "Synchronisation activée, mise à jour sur le serveur")
                    withContext(Dispatchers.IO) {
                        try {
                            onlineUserService.toggleFavorite(id)
                            Log.d("UserRepository", "Statut favori changé sur le serveur avec succès")
                        } catch (e: Exception) {
                            Log.e("UserRepository", "Erreur lors de la mise à jour sur le serveur", e)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("UserRepository", "Erreur lors de la mise à jour sur le serveur", e)
                }
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Erreur lors du changement de statut favori", e)
        } finally {
            // IMPORTANT: Rafraîchir l'interface utilisateur en s'assurant que c'est fait sur le thread principal
            withContext(Dispatchers.Main) {
                try {
                    Log.d("UserRepository", "Début du rafraîchissement de l'interface utilisateur")
                    refreshUsers()
                    Log.d("UserRepository", "Interface utilisateur rafraîchie après changement de statut favori")
                } catch (e: Exception) {
                    Log.e("UserRepository", "Erreur lors du rafraîchissement de l'interface utilisateur", e)
                }
            }
        }
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
    // Modified syncData method in UserRepositoryImpl class
    override suspend fun syncData() {
        Log.d("UserRepository", "Démarrage de la synchronisation")
        if (!dataStoreManager.isSyncEnabled()) {
            Log.d("UserRepository", "Synchronisation désactivée")
            return
        }

        try {
            // Synchronize data based on the current source
            val currentSource = getCurrentDataSource()
            Log.d("UserRepository", "Source de données actuelle: $currentSource")

            when (currentSource) {
                DataSource.MEMORY, DataSource.DATABASE, DataSource.ONLINE -> {
                    Log.d("UserRepository", "Synchronisation de ${currentSource.name} vers ONLINE")
                    val success = syncLocalToOnline()
                    Log.d("UserRepository", "Résultat de la synchronisation: ${if (success) "Succès" else "Échec"}")
                }
//                DataSource.ONLINE -> {
//                    Log.d("UserRepository", "Synchronisation depuis ONLINE vers local")
//                    // Sync from online to local
//                    val onlineUsers = onlineUserService.getAll()
//                    Log.d("UserRepository", "Récupération de ${onlineUsers.size} utilisateurs en ligne")
//
//                    // Sync to memory
//                    onlineUsers.forEach { user ->
//                        val existingUser = memoryUserService.getById(user.id)
//                        if (existingUser == null) {
//                            memoryUserService.add(user)
//                        } else {
//                            memoryUserService.update(user)
//                        }
//                    }
//
//                    // Sync to database
//                    onlineUsers.forEach { user ->
//                        val existingUser = databaseUserService.getById(user.id)
//                        if (existingUser == null) {
//                            databaseUserService.add(user)
//                        } else {
//                            databaseUserService.update(user)
//                        }
//                    }
//                }
            }

            refreshUsers()
            Log.d("UserRepository", "Synchronisation terminée avec succès")
        } catch (e: Exception) {
            Log.e("UserRepository", "Erreur lors de la synchronisation", e)
            throw e
        }
    }

    // Improved syncLocalToOnline method
    suspend fun syncLocalToOnline(): Boolean = withContext(Dispatchers.IO) {
        Log.d("UserRepository", "Début de syncLocalToOnline")
        try {
            // 1. Récupérer toutes les données de la source locale
            val localUsers = when (getCurrentDataSource()) {
                DataSource.MEMORY -> {
                    Log.d("UserRepository", "Récupération des utilisateurs depuis MEMORY")
                    memoryUserService.getAll()
                }
                DataSource.DATABASE -> {
                    Log.d("UserRepository", "Récupération des utilisateurs depuis DATABASE")
                    databaseUserService.getAll()
                }
                DataSource.ONLINE -> {
                    Log.d("UserRepository", "Déjà en mode ONLINE, rien à synchroniser")
                    return@withContext true
                }
            }

            Log.d("UserRepository", "Nombre d'utilisateurs locaux: ${localUsers.size}")

            // Si aucune donnée locale, rien à synchroniser
            if (localUsers.isEmpty()) {
                Log.d("UserRepository", "Aucun utilisateur local, rien à synchroniser")
                return@withContext true
            }

            // 2. Ajouter tous les utilisateurs locaux à la base distante
            var successCount = 0
            for (localUser in localUsers) {
                try {
                    Log.d("UserRepository", "Ajout de l'utilisateur ${localUser.name} (ID: ${localUser.id}) à la base distante")
                    val addedUser = onlineUserService.add(localUser)
                    Log.d("UserRepository", "Utilisateur ajouté avec succès, nouvel ID: ${addedUser.id}")
                    successCount++
                } catch (e: Exception) {
                    Log.e("UserRepository", "Erreur lors de l'ajout de l'utilisateur ${localUser.id}", e)
                    // Continue avec les autres ajouts même si un échoue
                }
            }

            Log.d("UserRepository", "Synchronisation terminée. $successCount/${localUsers.size} utilisateurs synchronisés")
            return@withContext successCount > 0
        } catch (e: Exception) {
            Log.e("UserRepository", "Erreur lors de la synchronisation", e)
            return@withContext false
        }
    }
}