package com.vama.android.data.api

import com.vama.android.data.api.online.ApiService
import com.vama.android.data.api.online.UserDto
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
        apiService.getById(id)?.toUser()
    }

    override suspend fun add(user: User): User = withContext(Dispatchers.IO) {
        val userDto = UserDto.fromUser(user)
        val newUser = apiService.add(userDTO)
        newUser.toUser()
    }

    override suspend fun update(user: User): User = withContext(Dispatchers.IO) {
        val userDTO = UserDto.fromUser(user)
        val updatedUser = apiService.update(userDTO)
        updatedUser.toUser()
    }

    override suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
        apiService.delete(id)
    }

    override suspend fun search(query: String): List<User> = withContext(Dispatchers.IO) {
        apiService.search(query).map { it.toUser() }
    }

    override suspend fun toggleFavorite(id: Long) = withContext(Dispatchers.IO) {
        apiService.toggleFavorite(id)
    }

    override suspend fun getFavorites(): List<User> = withContext(Dispatchers.IO) {
        apiService.getFavorites().map { it.toUser() }
    }

    override suspend fun sortBy(criteria: SortCriteria): List<User> = withContext(Dispatchers.IO) {
        // D'abord, récupérer tous les utilisateurs
        val users = getAll()

        // Ensuite, appliquer le tri en fonction du critère
        when (criteria) {
            SortCriteria.NAME_ASC -> users.sortedBy { it.name }
            SortCriteria.NAME_DESC -> users.sortedByDescending { it.name }
            SortCriteria.DATE_ASC -> users.sortedBy { it.id } // Supposant que l'ID augmente avec la date de création
            SortCriteria.DATE_DESC -> users.sortedByDescending { it.id }
            SortCriteria.FAVORITE -> users.sortedBy { it.favorite }
            SortCriteria.DISTANCE -> users.sortedBy { it.address }
        }
    }
}