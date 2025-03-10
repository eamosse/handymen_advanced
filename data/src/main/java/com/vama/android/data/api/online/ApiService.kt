package com.vama.android.data.api.online

import retrofit2.http.*

/**
 * Interface Retrofit pour les opérations API relatives aux utilisateurs
 */
interface ApiService {
    /**
     * Récupère tous les utilisateurs
     */
    @GET("users")
    suspend fun getAll(): List<UserDTO>

    /**
     * Récupère un utilisateur par son ID
     */
    @GET("users/{id}")
    suspend fun getById(@Path("id") id: Long): UserDTO?

    /**
     * Ajoute un nouvel utilisateur
     */
    @POST("users")
    suspend fun add(@Body user: UserDTO): UserDTO

    /**
     * Met à jour un utilisateur existant
     */
    @PUT("users/{id}")
    suspend fun update(@Path("id") id: Long, @Body user: UserDTO): UserDTO

    /**
     * Version simplifiée de la méthode update qui extrait l'ID de l'objet utilisateur
     */
    @PUT("users/{id}")
    suspend fun update(@Body user: UserDTO): UserDTO {
        return update(user.id, user)
    }

    /**
     * Supprime un utilisateur
     */
    @DELETE("users/{id}")
    suspend fun delete(@Path("id") id: Long)

    /**
     * Recherche des utilisateurs par un terme de recherche
     */
    @GET("users/search")
    suspend fun search(@Query("q") query: String): List<UserDTO>

    /**
     * Bascule l'état favori d'un utilisateur
     */
    @POST("users/{id}/toggle-favorite")
    suspend fun toggleFavorite(@Path("id") id: Long)

    /**
     * Récupère tous les utilisateurs favoris
     */
    @GET("users/favorites")
    suspend fun getFavorites(): List<UserDTO>
}

