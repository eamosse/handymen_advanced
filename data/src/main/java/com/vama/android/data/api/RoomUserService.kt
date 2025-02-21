package com.vama.android.data.api

import com.vama.android.data.database.AppDatabase
import com.vama.android.data.database.UserEntity
import com.vama.android.data.model.User

class RoomUserService(private val database: AppDatabase) : UserService {
    private val userDao = database.userDao()

    override fun getAll(): List<User> =
        userDao.getAll().map { it.toUser() }

    override fun getById(id: Long): User? =
        userDao.getById(id)?.toUser()

    override fun add(user: User): User {
        val entity = UserEntity.fromUser(user)
        val newId = userDao.insert(entity)
        return user.copy(id = newId)
    }

    override fun update(user: User): User {
        val entity = UserEntity.fromUser(user)
        userDao.update(entity)
        return user
    }

    override fun delete(id: Long) {
        userDao.delete(id)
    }

    override fun search(query: String): List<User> =
        userDao.search(query).map { it.toUser() }

    override fun toggleFavorite(id: Long) {
        userDao.toggleFavorite(id)
    }

    override fun getFavorites(): List<User> =
        userDao.getFavorites().map { it.toUser() }

    override fun sortBy(criteria: SortCriteria): List<User> {
        return when (criteria) {
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
