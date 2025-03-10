package com.vama.android.data.api.online

import retrofit2.http.*

interface ApiService {

    @GET("handymen")
    suspend fun getAll(): List<UserResponse>

    @GET("handymen/{id}")
    suspend fun get(@Path("id") id: String): UserResponse



    @GET("handymen")
    suspend fun search(): List<UserResponse>

    @POST("handymen")
    suspend fun create(@Body handyman: UserRequest): UserResponse

    @PUT("handymen/{id}")
    suspend fun update(@Path("id") id: String, @Body handyman: UserRequest): UserResponse

    @DELETE("handymen/{id}")
    suspend fun delete(@Path("id") id: String)
}