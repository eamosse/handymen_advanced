package com.vama.android.data.api

import com.vama.android.data.model.User
import com.vama.android.data.utils.Dummy_Users

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


internal class InMemoryUserService : UserService {
    private val _users = Dummy_Users.toMutableList()

    override suspend fun getAll(): List<User> = _users.toList()

    override suspend fun getById(id: Long): User? = _users.find { it.id == id }

    override suspend fun add(user: User): User {
        // Ensure unique ID by taking max ID + 1
        val newUser = user.copy(id = _users.maxOfOrNull { it.id }?.plus(1) ?: 1)
        _users.add(newUser)
        return newUser
    }

    override suspend fun update(user: User): User {
        val index = _users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            _users[index] = user
            return user
        }
        throw IllegalArgumentException("User not found with id: ${user.id}")
    }

    override suspend fun delete(id: Long) {
        _users.removeIf { it.id == id }
    }

    override suspend fun search(query: String): List<User> {
        val lowercaseQuery = query.lowercase()
        return _users.filter { user ->
            user.name.lowercase().contains(lowercaseQuery) ||
                    user.phoneNumber.contains(query) ||
                    user.address.lowercase().contains(lowercaseQuery)
        }
    }

    override suspend fun toggleFavorite(id: Long) {
        val index = _users.indexOfFirst { it.id == id }
        if (index != -1) {
            val user = _users[index]
            _users[index] = user.copy(favorite = !user.favorite)
        }
    }

    override suspend fun getFavorites(): List<User> = _users.filter { it.favorite }

    override suspend fun sortBy(criteria: SortCriteria): List<User> {
        return when (criteria) {
            SortCriteria.NAME_ASC -> _users.sortedBy { it.name }
            SortCriteria.NAME_DESC -> _users.sortedByDescending { it.name }
            SortCriteria.FAVORITE -> _users.sortedByDescending { it.favorite }
            SortCriteria.DISTANCE -> _users.sortedBy {
                // Extract distance from address (format: "Location ; Xkm")
                val distanceStr = it.address.split(";").getOrNull(1)?.trim()?.replace("km", "") ?: "0"
                distanceStr.toIntOrNull() ?: 0
            }
        }
    }
}