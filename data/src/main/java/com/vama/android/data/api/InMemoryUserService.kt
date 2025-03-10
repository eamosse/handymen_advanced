package com.vama.android.data.api

import com.vama.android.data.model.User
import com.vama.android.data.utils.Dummy_Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InMemoryUserService @Inject constructor() : UserService {
    private val _users = Dummy_Users.toMutableList()

    override suspend fun getAll(): List<User> = withContext(Dispatchers.IO) {
        _users.toList()
    }

    override suspend fun getById(id: Long): User? = withContext(Dispatchers.IO) {
        _users.find { it.id == id }
    }

    override suspend fun add(user: User): User = withContext(Dispatchers.IO) {
        val newUser = user.copy(id = _users.maxOfOrNull { it.id }?.plus(1) ?: 1)
        _users.add(newUser)
        newUser
    }

    override suspend fun update(user: User): User = withContext(Dispatchers.IO) {
        val index = _users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            _users[index] = user
            user
        } else {
            throw IllegalArgumentException("User not found with id: ${user.id}")
        }
    }

    override suspend fun delete(id: Long): Unit = withContext(Dispatchers.IO) {
        _users.removeIf { it.id == id }
    }

    override suspend fun search(query: String): List<User> = withContext(Dispatchers.IO) {
        val lowercaseQuery = query.lowercase()
        _users.filter { user ->
            user.name.lowercase().contains(lowercaseQuery) ||
                    user.phoneNumber.contains(query) ||
                    user.address.lowercase().contains(lowercaseQuery)
        }
    }

    override suspend fun toggleFavorite(id: Long) = withContext(Dispatchers.IO) {
        val index = _users.indexOfFirst { it.id == id }
        if (index != -1) {
            val user = _users[index]
            _users[index] = user.copy(favorite = !user.favorite)
        }
    }

    override suspend fun getFavorites(): List<User> = withContext(Dispatchers.IO) {
        _users.filter { it.favorite }
    }

    override suspend fun sortBy(criteria: SortCriteria): List<User> = withContext(Dispatchers.IO) {
        when (criteria) {
            SortCriteria.NAME_ASC -> _users.sortedBy { it.name }
            SortCriteria.NAME_DESC -> _users.sortedByDescending { it.name }
            SortCriteria.FAVORITE -> _users.sortedByDescending { it.favorite }
            SortCriteria.DISTANCE -> _users.sortedBy {
                val distanceStr = it.address.split(";").getOrNull(1)?.trim()?.replace("km", "") ?: "0"
                distanceStr.toIntOrNull() ?: 0
            }
        }
    }
}