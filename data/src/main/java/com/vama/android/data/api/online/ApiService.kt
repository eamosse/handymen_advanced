package com.vama.android.data.api.online

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("handymen")
    suspend fun getAll(): List<UserResponse>

    @GET("handymen/{id}") // Devient handymen/1245e7
    suspend fun get(@Path("id") id:String): UserResponse

    @GET("handymen/search")  // Devient neighbors/search?filter=query
    suspend fun search(@Query("filter") search: String): List<UserResponse>

    @POST("handymen")
    suspend fun create(@Body neighbor: UserRequest): UserResponse


}

