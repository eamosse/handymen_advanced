package com.vama.android.data.database

import com.vama.android.data.database.mappers.toEntity
import com.vama.android.data.database.mappers.toUser
import com.vama.android.data.model.AddUser
import com.vama.android.data.model.Identity
import com.vama.android.data.model.SortCriteria
import com.vama.android.data.model.User
import com.vama.android.data.services.UserService
import com.vama.android.data.utils.DataResult
import com.vama.android.data.utils.safeCall
import javax.inject.Inject

private fun Identity.asLocal() =
    localId ?: throw IllegalArgumentException("User does not have an id")

class RoomUserService @Inject constructor(
    database: AppDatabase
) : UserService {
    private val userDao = database.userDao()

    override suspend fun getAll(): DataResult<List<User>> = safeCall {
        val users = userDao.getAll().map { it.toUser() }
        DataResult.Success(users)
    }

    override suspend fun getById(id: Identity): DataResult<User> = safeCall {
        userDao.getById(id.asLocal())?.toUser()?.let {
            DataResult.Success(it)
        } ?: DataResult.Error(
            Exception("User not found"), code = 404, message = "User not found"
        )
    }

    override suspend fun add(user: AddUser): DataResult<User> = safeCall {
        val entity = user.toEntity()
        userDao.insert(entity)
        DataResult.Success(entity.toUser().copy(id = Identity(entity.id, null)))
    }

    override suspend fun update(user: User): DataResult<User> = safeCall {
        val entity = user.toEntity()
        userDao.update(entity)
        DataResult.Success(user)
    }

    override suspend fun delete(id: Identity): DataResult<Unit> = safeCall {
        userDao.delete(id.asLocal())
        DataResult.Success(Unit)
    }

    override suspend fun search(query: String): DataResult<List<User>> = safeCall {
        val users = userDao.search(query).map { it.toUser() }
        DataResult.Success(users)
    }

    override suspend fun toggleFavorite(id: Identity, toggle: Boolean): DataResult<User> =
        safeCall {
            userDao.toggleFavorite(id.asLocal())
            getById(id)
        }

    override suspend fun getFavorites(): DataResult<List<User>> = safeCall {
        val users = userDao.getFavorites().map { it.toUser() }
        DataResult.Success(users)
    }

    override suspend fun sortBy(criteria: SortCriteria): DataResult<List<User>> = safeCall {
        DataResult.Error(
            Exception("Not implemented"),
            code = 501,
            message = "Not implemented. You should build appropriate sorting logic using the DAO"
        )
    }

    override suspend fun sync(users: List<User>): DataResult<Unit> {
        TODO("Not yet implemented")
    }
}
