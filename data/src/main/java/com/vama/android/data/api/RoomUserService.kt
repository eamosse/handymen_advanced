package com.vama.android.data.api

import com.vama.android.data.database.AppDatabase
import com.vama.android.data.database.UserEntity
import com.vama.android.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomUserService @Inject constructor(
    private val database: AppDatabase
) : UserService {
    private val userDao = database.userDao()

    override suspend fun getAll(): List<User> = withContext(Dispatchers.IO) {
        userDao.getAll().map { it.toUser() }
    }

    override suspend fun getById(id: Long): User? = withContext(Dispatchers.IO) {
        userDao.getById(id)?.toUser()
    }

    override suspend fun add(user: User): User = withContext(Dispatchers.IO) {
        val entity = UserEntity.fromUser(user)
        val newId = userDao.insert(entity)
        user.copy(id = newId)
    }

    override suspend fun update(user: User): User = withContext(Dispatchers.IO) {
        val entity = UserEntity.fromUser(user)
        userDao.update(entity)
        user
    }

    override suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
        userDao.delete(id)
    }

    override suspend fun search(query: String): List<User> = withContext(Dispatchers.IO) {
        userDao.search(query).map { it.toUser() }
    }

    override suspend fun toggleFavorite(id: Long) = withContext(Dispatchers.IO) {
        userDao.toggleFavorite(id)
    }

    override suspend fun getFavorites(): List<User> = withContext(Dispatchers.IO) {
        userDao.getFavorites().map { it.toUser() }
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
