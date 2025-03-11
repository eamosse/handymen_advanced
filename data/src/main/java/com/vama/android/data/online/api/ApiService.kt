package com.vama.android.data.online.api

import com.vama.android.data.model.SortCriteria
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // TODO : Handle the sorting criteria in the API endpoint
    @GET("handymen")
    suspend fun getAll(@Query("sort") sortCriteria: SortCriteria = SortCriteria.DISTANCE): Response<List<UserResponse>>

    @GET("handymen/favorites")
    suspend fun getFavorites(): Response<List<UserResponse>>

    @GET("handymen/{id}")
    suspend fun get(@Path("id") id: String): Response<UserResponse>

    @GET("handymen")
    suspend fun search(@Query("search") query: String): Response<List<UserResponse>>

    @POST("handymen")
    suspend fun create(@Body handyman: UserRequest): Response<UserResponse>

    @PUT("handymen/{id}")
    suspend fun update(@Path("id") id: String, @Body handyman: UserRequest): Response<UserResponse>

    @PUT("handymen/{id}/{toggle}")
    // TODO : Create an endpoint to toggle the favorite status of a user, you just need to pass the id of the user and the flag to toggle
    suspend fun toggle(
        @Path("id") id: String,
        @Path("toggle") toggle: Boolean
    ): Response<UserResponse>

    @DELETE("handymen/{id}")
    suspend fun delete(@Path("id") id: String): Response<Unit>

    @POST("handymen/sync")
    // TODO : Create an endpoint to sync the users, you just need to pass the list of users to sync
    fun sync(users: List<UserRequest>): Response<Unit>
}