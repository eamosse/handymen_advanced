package com.vama.android.data.api

import com.vama.android.data.model.User

interface UserService {
    suspend fun getAll(): List<User>
    suspend fun getById(id: Long): User?
    suspend fun add(user: User): User
    suspend fun update(user: User): User
    suspend fun delete(id: Long)
    suspend fun search(query: String): List<User>
    suspend fun toggleFavorite(id: Long)
    suspend fun getFavorites(): List<User>
    suspend fun sortBy(criteria: SortCriteria): List<User>
}
