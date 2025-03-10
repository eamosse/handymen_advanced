package com.vama.android.data.api

import com.vama.android.data.api.online.ApiService
import com.vama.android.data.api.online.UserRequest
import com.vama.android.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OnlineUserService @Inject constructor(
    private val apiService: ApiService
) : UserService {

    override suspend fun getAll(): List<User> = withContext(Dispatchers.IO) {
        apiService.getAll().map { it.toUser() }
    }

    override suspend fun getById(id: Long): User? = withContext(Dispatchers.IO) {
        try {
            apiService.get(id.toString()).toUser()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun add(user: User): User = withContext(Dispatchers.IO) {
        val request = UserRequest(
            name = user.name,
            phoneNumber = user.phoneNumber,
            address = user.address,
            favorite = user.favorite
        )
        val response = apiService.create(request)
        response.toUser()
    }

    override suspend fun update(user: User): User = withContext(Dispatchers.IO) {
        // API doesn't have an update endpoint, so we'll need to implement this later
        // For now, return the unchanged user
        user
    }

    override suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
        // API doesn't have a delete endpoint, so we'll need to implement this later
    }

    override suspend fun search(query: String): List<User> = withContext(Dispatchers.IO) {
        apiService.search(query).map { it.toUser() }
    }

    override suspend fun toggleFavorite(id: Long) = withContext(Dispatchers.IO) {
        // API doesn't have a toggleFavorite endpoint, so we'll need to implement this later
    }

    override suspend fun getFavorites(): List<User> = withContext(Dispatchers.IO) {
        // API doesn't have a getFavorites endpoint
        // We'll filter favorites from all users as a workaround
        getAll().filter { it.favorite }
    }

    override suspend fun sortBy(criteria: SortCriteria): List<User> = withContext(Dispatchers.IO) {
        when (criteria) {
            SortCriteria.NAME_ASC -> getAll().sortedBy { it.name }
            SortCriteria.NAME_DESC -> getAll().sortedByDescending { it.name }
            SortCriteria.FAVORITE -> getAll().sortedByDescending { it.favorite }
            SortCriteria.DISTANCE -> getAll().sortedBy {
                val distanceStr = it.address.split(";").getOrNull(1)?.trim()?.replace("km", "") ?: "0"
                distanceStr.toIntOrNull() ?: 0
            }
        }
    }
}