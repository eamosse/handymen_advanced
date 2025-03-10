package com.vama.android.data.api

import com.vama.android.data.api.online.ApiService
import com.vama.android.data.database.AppDatabase
import com.vama.android.data.database.UserEntity
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

    // TODO: Implémenter les autres méthodes de UserService
//    override suspend fun getById(id: Long): User? = withContext(Dispatchers.IO) {
//        apiService.getById(id)?.toUser()
//    }
//
//    override suspend fun add(user: User): User = withContext(Dispatchers.IO) {
//        val entity = UserEntity.fromUser(user)
//        val newId = apiService.insert(entity)
//        user.copy(id = newId)
//    }
//
//    override suspend fun update(user: User): User = withContext(Dispatchers.IO) {
//        val entity = UserEntity.fromUser(user)
//        apiService.update(entity)
//        user
//    }
//
//    override suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
//        apiService.delete(id)
//    }
//
//    override suspend fun search(query: String): List<User> = withContext(Dispatchers.IO) {
//        apiService.search(query).map { it.toUser() }
//    }
//
//    override suspend fun toggleFavorite(id: Long) = withContext(Dispatchers.IO) {
//        apiService.toggleFavorite(id)
//    }
//
//    override suspend fun getFavorites(): List<User> = withContext(Dispatchers.IO) {
//        apiService.getFavorites().map { it.toUser() }
//    }
//
//    override suspend fun sortBy(criteria: SortCriteria): List<User> = withContext(Dispatchers.IO) {
//        when (criteria) {
//            SortCriteria.NAME_ASC -> getAll().sortedBy { it.name }
//            SortCriteria.NAME_DESC -> getAll().sortedByDescending { it.name }
//            SortCriteria.FAVORITE -> getAll().sortedByDescending { it.favorite }
//            SortCriteria.DISTANCE -> getAll().sortedBy {
//                val distanceStr = it.address.split(";").getOrNull(1)?.trim()?.replace("km", "") ?: "0"
//                distanceStr.toIntOrNull() ?: 0
//            }
//        }
//    }
}
