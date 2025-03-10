package com.vama.android.data.api

import android.util.Log
import com.vama.android.data.api.online.ApiService
import com.vama.android.data.api.online.UserRequest
import com.vama.android.data.api.online.UserResponse
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
            Log.d("OnlineUserService", "Suppression de l'utilisateur avec ID: $id")

            // Vérifier si nous avons un ID MongoDB correspondant
            val mongoId = UserResponse.getMongoId(id)

            if (mongoId != null) {
                // Utiliser l'ID MongoDB que nous avons en cache
                Log.d("OnlineUserService", "ID MongoDB correspondant trouvé en cache: $mongoId")
                apiService.delete(mongoId)
                Log.d("OnlineUserService", "Suppression réussie sur le serveur")
            } else {
                // Si nous n'avons pas d'ID en cache, chercher dans tous les utilisateurs
                Log.d("OnlineUserService", "Pas d'ID MongoDB en cache, recherche parmi tous les utilisateurs")
                val allUsers = apiService.getAll()
                val matchingUser = allUsers.find { it.toUser().id == id }

                if (matchingUser != null) {
                    // Utiliser l'ID MongoDB trouvé
                    val foundMongoId = matchingUser.id
                    Log.d("OnlineUserService", "ID MongoDB correspondant trouvé: $foundMongoId")
                    apiService.delete(foundMongoId)
                    Log.d("OnlineUserService", "Suppression réussie sur le serveur")
                } else {
                    // Dernier recours : essayer avec l'ID local
                    Log.d("OnlineUserService", "Aucun utilisateur correspondant trouvé, tentative avec l'ID local")
                    apiService.delete(id.toString())
                }
            }
        } catch (e: Exception) {
            // Log détaillé de l'erreur
            Log.e("OnlineUserService", "Erreur lors de la suppression de l'utilisateur", e)
            if (e is retrofit2.HttpException) {
                try {
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("OnlineUserService", "Corps de l'erreur: $errorBody")

                    // Dans le cas où l'erreur est 404 (Not Found), on peut considérer que c'est un succès
                    // car l'utilisateur n'existe plus sur le serveur
                    if (e.code() == 404) {
                        Log.d("OnlineUserService", "L'utilisateur n'existe pas sur le serveur (404), considéré comme supprimé")
                        return@withContext
                    }
                } catch (e2: Exception) {
                    // Ignorer si on ne peut pas lire le corps de l'erreur
                }
            }
            throw e // Propager l'erreur pour informer l'appelant
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