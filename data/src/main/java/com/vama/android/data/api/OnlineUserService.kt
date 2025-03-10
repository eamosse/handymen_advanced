package com.vama.android.data.api

import android.util.Log
import com.vama.android.data.api.online.ApiService
import com.vama.android.data.api.online.UserRequest
import com.vama.android.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class OnlineUserService @Inject constructor(
    private val apiService: ApiService
) : UserService {

    override suspend fun getAll(): List<User> = withContext(Dispatchers.IO) {
        try {
            Log.d("OnlineUserService", "Récupération de tous les utilisateurs")
            val users = apiService.getAll().map { it.toUser() }
            Log.d("OnlineUserService", "Récupération réussie: ${users.size} utilisateurs")
            users
        } catch (e: Exception) {
            // Log the error and return empty list
            Log.e("OnlineUserService", "Erreur lors de la récupération des utilisateurs", e)
            emptyList()
        }
    }


    override suspend fun getById(id: Long): User? = withContext(Dispatchers.IO) {
        try {
            apiService.get(id.toString()).toUser()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun add(user: User): User = withContext(Dispatchers.IO) {
        val request = UserRequest(
            name = user.name,
            phone = user.phoneNumber,
            address = user.address,
            isFavorite = user.favorite,
            avatarUrl = user.avatarUrl,
            aboutMe = user.aboutMe,
            webSite = user.webSite
        )

        Log.d("OnlineUserService", "Ajout d'un utilisateur: ${user.name}")

        try {
            val response = apiService.create(request)
            Log.d("OnlineUserService", "Réponse du serveur: $response")

            // Transformation de la réponse en User
            val createdUser = response.toUser()
            Log.d("OnlineUserService", "Utilisateur créé avec l'ID: ${createdUser.id}")

            createdUser
        } catch (e: Exception) {
            // If API fails, return the original user (should be handled better in production)
            Log.e("OnlineUserService", "Erreur lors de l'ajout de l'utilisateur", e)
            if (e is retrofit2.HttpException) {
                try {
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("OnlineUserService", "Corps de l'erreur: $errorBody")
                } catch (e2: Exception) {
                    // Ignorer si on ne peut pas lire le corps de l'erreur
                }
            }
            user
        }
    }

    override suspend fun update(user: User): User = withContext(Dispatchers.IO) {
        try {
            val request = UserRequest(
                name = user.name,
                phone = user.phoneNumber,
                address = user.address,
                isFavorite = user.favorite,
                avatarUrl = user.avatarUrl,
                aboutMe = user.aboutMe,
                webSite = user.webSite
            )
            // API supporte maintenant les mises à jour
            val response = apiService.update(user.id.toString(), request)
            response.toUser()
        } catch (e: Exception) {
            e.printStackTrace()
            user
        }
    }

    override suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
        try {
            // Assuming the API has been updated to support deletion
            apiService.delete(id.toString())
        } catch (e: Exception) {
            // If the API doesn't support deletion yet, log the error
            e.printStackTrace()
        }
    }

    override suspend fun search(query: String): List<User> = withContext(Dispatchers.IO) {
        try {
            // Comme le serveur n'a pas d'endpoint de recherche, nous récupérons tous les utilisateurs
            // et filtrons côté client
            val allUsers = apiService.search().map { it.toUser() }
            // Filtre par nom, numéro de téléphone ou adresse contenant la requête (insensible à la casse)
            allUsers.filter { user ->
                user.name.contains(query, ignoreCase = true) ||
                        user.phoneNumber.contains(query, ignoreCase = true) ||
                        user.address.contains(query, ignoreCase = true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun toggleFavorite(id: Long) = withContext(Dispatchers.IO) {
        try {
            // Get the current user
            val user = getById(id) ?: return@withContext

            // Toggle favorite status
            val updated = user.copy(favorite = !user.favorite)

            // Update the user
            update(updated)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getFavorites(): List<User> = withContext(Dispatchers.IO) {
        try {
            // API doesn't have a getFavorites endpoint
            // We'll filter favorites from all users as a workaround
            getAll().filter { it.favorite }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun sortBy(criteria: SortCriteria): List<User> = withContext(Dispatchers.IO) {
        when (criteria) {
            SortCriteria.NAME_ASC -> getAll().sortedBy { it.name }
            SortCriteria.NAME_DESC -> getAll().sortedByDescending { it.name }
            SortCriteria.FAVORITE -> getAll().sortedByDescending { it.favorite }
            SortCriteria.DISTANCE -> getAll().sortedBy {
                val distanceStr = it.address.split(";").getOrNull(1)?.trim()?.replace("km", "") ?: "0"
                distanceStr.toIntOrNull() ?: 0
            }
        }
    }
}