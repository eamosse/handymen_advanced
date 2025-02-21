package com.vama.android.data.repositories

import com.vama.android.data.api.SortCriteria
import com.vama.android.data.api.UserService
import com.vama.android.data.model.User
import javax.inject.Inject

interface UserRepository {
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

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserRepository {
    override suspend fun getAll(): List<User> = userService.getAll()
    override suspend fun getById(id: Long): User? = userService.getById(id)
    override suspend fun add(user: User): User = userService.add(user)
    override suspend fun update(user: User): User = userService.update(user)
    override suspend fun delete(id: Long) = userService.delete(id)
    override suspend fun search(query: String): List<User> = userService.search(query)
    override suspend fun toggleFavorite(id: Long) = userService.toggleFavorite(id)
    override suspend fun getFavorites(): List<User> = userService.getFavorites()
    override suspend fun sortBy(criteria: SortCriteria): List<User> = userService.sortBy(criteria)
}