package com.vama.android.data.api

import com.vama.android.data.database.AppDatabase
import com.vama.android.data.database.toEntity
import com.vama.android.data.database.toNewEntity
import com.vama.android.data.database.toUser
import com.vama.android.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomUserService @Inject constructor(
    private val database: AppDatabase
) : UserService {

    override suspend fun getAll(): List<User> = withContext(Dispatchers.IO) {
        database.userDao().getAll().map { it.toUser() }
    }

    override suspend fun getById(id: Long): User? = withContext(Dispatchers.IO) {
        database.userDao().getById(id)?.toUser()
    }

    override suspend fun add(user: User): User = withContext(Dispatchers.IO) {
        // Utilisation de toNewEntity pour s'assurer que l'ID est 0 et sera généré
        val newId = database.userDao().insert(user.toNewEntity())
        return@withContext user.copy(id = newId)
    }

    override suspend fun update(user: User): User = withContext(Dispatchers.IO) {
        // Utilisation de toEntity pour préserver l'ID existant
        database.userDao().update(user.toEntity())
        return@withContext user
    }

    override suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
        database.userDao().delete(id)
    }

    override suspend fun search(query: String): List<User> = withContext(Dispatchers.IO) {
        val searchTerm = "%$query%"
        database.userDao().search(searchTerm).map { it.toUser() }
    }

    override suspend fun toggleFavorite(id: Long): Unit = withContext(Dispatchers.IO) {
        val user = database.userDao().getById(id)
        user?.let {
            val updatedUser = it.copy(favorite = !it.favorite)
            database.userDao().update(updatedUser)
        }
    }

    override suspend fun getFavorites(): List<User> = withContext(Dispatchers.IO) {
        database.userDao().getFavorites().map { it.toUser() }
    }

    override suspend fun sortBy(criteria: SortCriteria): List<User> = withContext(Dispatchers.IO) {
        val users = when (criteria) {
            SortCriteria.NAME_ASC -> database.userDao().getUsersSortedByNameAsc()
            SortCriteria.NAME_DESC -> database.userDao().getUsersSortedByNameDesc()
            SortCriteria.DATE_ASC -> database.userDao().getUsersSortedByIdAsc()
            SortCriteria.DATE_DESC -> database.userDao().getUsersSortedByIdDesc()
            SortCriteria.FAVORITE -> database.userDao().getUsersSortedByFavorites()
            SortCriteria.DISTANCE -> database.userDao().getUsersSortedByDistance()
        }
        return@withContext users.map { it.toUser() }
    }
}