package com.vama.android.data.api

import com.vama.android.data.model.User
import com.vama.android.data.utils.Dummy_Users

interface UserService {
    fun getAll(): List<User>
    fun getById(id: Long): User?
    fun add(user: User): User
    fun update(user: User): User
    fun delete(id: Long)
    fun search(query: String): List<User>
    fun toggleFavorite(id: Long)
    fun getFavorites(): List<User>
    fun sortBy(criteria: SortCriteria): List<User>
}


internal class InMemoryUserService : UserService {
    private val _users = Dummy_Users.toMutableList()

    override fun getAll(): List<User> = _users.toList()

    override fun getById(id: Long): User? = _users.find { it.id == id }

    override fun add(user: User): User {
        // Ensure unique ID by taking max ID + 1
        val newUser = user.copy(id = _users.maxOfOrNull { it.id }?.plus(1) ?: 1)
        _users.add(newUser)
        return newUser
    }

    override fun update(user: User): User {
        val index = _users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            _users[index] = user
            return user
        }
        throw IllegalArgumentException("User not found with id: ${user.id}")
    }

    override fun delete(id: Long) {
        _users.removeIf { it.id == id }
    }

    override fun search(query: String): List<User> {
        val lowercaseQuery = query.lowercase()
        return _users.filter { user ->
            user.name.lowercase().contains(lowercaseQuery) ||
                    user.phoneNumber.contains(query) ||
                    user.address.lowercase().contains(lowercaseQuery)
        }
    }

    override fun toggleFavorite(id: Long) {
        val index = _users.indexOfFirst { it.id == id }
        if (index != -1) {
            val user = _users[index]
            _users[index] = user.copy(favorite = !user.favorite)
        }
    }

    override fun getFavorites(): List<User> = _users.filter { it.favorite }

    override fun sortBy(criteria: SortCriteria): List<User> {
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