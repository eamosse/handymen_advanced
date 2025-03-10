package com.vama.android.data.api.online

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("handymen")
    suspend fun getAll(): List<UserResponse>

    @GET("handymen/{id}")
    suspend fun get(@retrofit2.http.Path("id") id: String): UserResponse

    @GET("handymen/search")
    suspend fun search(@retrofit2.http.Query("filter") search: String): List<UserResponse>

    @POST("handymen")
    suspend fun create(@retrofit2.http.Body neighbor: UserRequest): UserResponse
}