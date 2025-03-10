package com.vama.android.data.api.online

import retrofit2.http.*

interface ApiService {

    @GET("handymen")
    suspend fun getAll(): List<UserResponse>

    @GET("handymen/{id}")
    suspend fun get(@Path("id") id: String): UserResponse

    // Note: La recherche n'est pas implémentée dans le serveur, donc cette méthode ne fonctionnera pas
    // Nous devrons faire une recherche côté client
    @GET("handymen")
    suspend fun search(): List<UserResponse> // On récupère tous et on filtre côté client

    @POST("handymen")
    suspend fun create(@Body handyman: UserRequest): UserResponse

    @PUT("handymen/{id}")
    suspend fun update(@Path("id") id: String, @Body handyman: UserRequest): UserResponse

    @DELETE("handymen/{id}")
    suspend fun delete(@Path("id") id: String)
}