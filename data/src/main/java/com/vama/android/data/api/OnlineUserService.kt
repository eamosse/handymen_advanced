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


            val createdUser = response.toUser()
            Log.d("OnlineUserService", "Utilisateur créé avec l'ID: ${createdUser.id}")

            createdUser
        } catch (e: Exception) {

            Log.e("OnlineUserService", "Erreur lors de l'ajout de l'utilisateur", e)
            if (e is retrofit2.HttpException) {
                try {
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("OnlineUserService", "Corps de l'erreur: $errorBody")
                } catch (e2: Exception) {

                }
            }
            user
        }
    }





    override suspend fun update(user: User): User = withContext(Dispatchers.IO) {
        try {
            Log.d("OnlineUserService", "Mise à jour de l'utilisateur avec ID: ${user.id}")

            val request = UserRequest(
                name = user.name,
                phone = user.phoneNumber,
                address = user.address,
                isFavorite = user.favorite,
                avatarUrl = user.avatarUrl,
                aboutMe = user.aboutMe,
                webSite = user.webSite
            )


            val mongoId = UserResponse.getMongoId(user.id)

            if (mongoId != null) {

                Log.d("OnlineUserService", "ID MongoDB correspondant trouvé en cache: $mongoId")
                val response = apiService.update(mongoId, request)
                Log.d("OnlineUserService", "Mise à jour réussie sur le serveur avec ID MongoDB en cache")
                return@withContext response.toUser()
            } else {

                Log.d("OnlineUserService", "Pas d'ID MongoDB en cache, recherche parmi tous les utilisateurs")
                val allUsers = apiService.getAll()
                val matchingUser = allUsers.find { it.toUser().id == user.id }

                if (matchingUser != null) {

                    val foundMongoId = matchingUser.id
                    Log.d("OnlineUserService", "ID MongoDB correspondant trouvé: $foundMongoId")
                    val response = apiService.update(foundMongoId, request)
                    Log.d("OnlineUserService", "Mise à jour réussie sur le serveur")


                    UserResponse.addMapping(foundMongoId, user.id)

                    return@withContext response.toUser()
                } else {

                    Log.d("OnlineUserService", "Aucun utilisateur correspondant trouvé, tentative avec l'ID local")
                    try {
                        val response = apiService.update(user.id.toString(), request)
                        Log.d("OnlineUserService", "Mise à jour réussie sur le serveur avec ID local")
                        return@withContext response.toUser()
                    } catch (e: Exception) {
                        Log.e("OnlineUserService", "Erreur lors de la mise à jour avec ID local", e)
                        if (e is HttpException) {
                            try {
                                val errorBody = e.response()?.errorBody()?.string()
                                Log.e("OnlineUserService", "Corps de l'erreur: $errorBody")
                            } catch (e2: Exception) {

                            }
                        }
                        throw e
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("OnlineUserService", "Erreur lors de la mise à jour de l'utilisateur", e)
            e.printStackTrace()
            return@withContext user
        }
    }

    override suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
        try {
            Log.d("OnlineUserService", "Suppression de l'utilisateur avec ID: $id")


            val mongoId = UserResponse.getMongoId(id)

            if (mongoId != null) {

                Log.d("OnlineUserService", "ID MongoDB correspondant trouvé en cache: $mongoId")
                apiService.delete(mongoId)
                Log.d("OnlineUserService", "Suppression réussie sur le serveur")
            } else {

                Log.d("OnlineUserService", "Pas d'ID MongoDB en cache, recherche parmi tous les utilisateurs")
                val allUsers = apiService.getAll()
                val matchingUser = allUsers.find { it.toUser().id == id }

                if (matchingUser != null) {

                    val foundMongoId = matchingUser.id
                    Log.d("OnlineUserService", "ID MongoDB correspondant trouvé: $foundMongoId")
                    apiService.delete(foundMongoId)
                    Log.d("OnlineUserService", "Suppression réussie sur le serveur")
                } else {

                    Log.d("OnlineUserService", "Aucun utilisateur correspondant trouvé, tentative avec l'ID local")
                    apiService.delete(id.toString())
                }
            }
        } catch (e: Exception) {

            Log.e("OnlineUserService", "Erreur lors de la suppression de l'utilisateur", e)
            if (e is retrofit2.HttpException) {
                try {
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("OnlineUserService", "Corps de l'erreur: $errorBody")



                    if (e.code() == 404) {
                        Log.d("OnlineUserService", "L'utilisateur n'existe pas sur le serveur (404), considéré comme supprimé")
                        return@withContext
                    }
                } catch (e2: Exception) {

                }
            }
            throw e
        }
    }

    override suspend fun search(query: String): List<User> = withContext(Dispatchers.IO) {
        try {


            val allUsers = apiService.search().map { it.toUser() }

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
            Log.d("OnlineUserService", "Basculement du statut favori pour l'utilisateur avec ID local: $id")


            val mongoId = UserResponse.getMongoId(id)

            if (mongoId != null) {

                Log.d("OnlineUserService", "ID MongoDB correspondant trouvé en cache: $mongoId")


                val allUsers = apiService.getAll()
                val matchingUser = allUsers.find { it.id == mongoId }

                if (matchingUser != null) {
                    Log.d("OnlineUserService", "Utilisateur trouvé avec ID MongoDB: $mongoId, nom: ${matchingUser.name}")


                    val request = UserRequest(
                        name = matchingUser.name,
                        phone = matchingUser.phoneNumber,
                        address = matchingUser.address,
                        isFavorite = !matchingUser.favorite,
                        avatarUrl = matchingUser.avatarUrl,
                        aboutMe = matchingUser.aboutMe,
                        webSite = matchingUser.webSite
                    )


                    val response = apiService.update(mongoId, request)
                    Log.d("OnlineUserService", "Mise à jour réussie sur le serveur avec l'ID MongoDB: $mongoId")
                    return@withContext
                }
            }


            Log.d("OnlineUserService", "Pas d'ID MongoDB en cache, recherche de l'utilisateur dans la liste complète")
            val allUsers = apiService.getAll()


            val matchingUserAndResponse = allUsers.map {
                val user = it.toUser()
                Pair(it, user)
            }.find { (_, user) ->
                user.id == id
            }

            if (matchingUserAndResponse != null) {
                val (response, user) = matchingUserAndResponse
                val foundMongoId = response.id

                Log.d("OnlineUserService", "Utilisateur trouvé dans la liste complète: ${user.name}, ID MongoDB: $foundMongoId")


                val request = UserRequest(
                    name = user.name,
                    phone = user.phoneNumber,
                    address = user.address,
                    isFavorite = !user.favorite,
                    avatarUrl = user.avatarUrl,
                    aboutMe = user.aboutMe,
                    webSite = user.webSite
                )


                apiService.update(foundMongoId, request)
                Log.d("OnlineUserService", "Mise à jour réussie sur le serveur avec l'ID MongoDB trouvé: $foundMongoId")


                UserResponse.addMapping(foundMongoId, id)

                return@withContext
            }


            Log.e("OnlineUserService", "Impossible de trouver l'utilisateur avec l'ID local: $id")
        } catch (e: Exception) {
            Log.e("OnlineUserService", "Erreur lors du basculement du statut favori pour l'utilisateur avec ID: $id", e)
            e.printStackTrace()
        }
    }

    override suspend fun getFavorites(): List<User> = withContext(Dispatchers.IO) {
        try {


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