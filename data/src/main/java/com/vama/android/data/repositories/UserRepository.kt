package com.vama.android.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vama.android.data.api.SortCriteria
import com.vama.android.data.api.UserService
import com.vama.android.data.model.User
import javax.inject.Inject

interface UserRepository {
    suspend fun getAll(): LiveData<List<User>>
    suspend fun getById(id: Long): User?
    suspend fun add(user: User): User
    suspend fun update(user: User): User
    suspend fun delete(id: Long)
    suspend fun search(query: String): List<User>
    suspend fun toggleFavorite(id: Long)
    suspend fun getFavorites(): List<User>
    suspend fun sortBy(criteria: SortCriteria): List<User>
}


// TODO : Le repository doit garder les filters et les tri afin de pouvoir les réappliquer après un refresh
class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserRepository {
    private val _users = MutableLiveData<List<User>>()
    private suspend fun refreshUsers() {
        _users.value = userService.getAll()
    }

    override suspend fun getAll(): LiveData<List<User>> {
        refreshUsers()
        return _users
    }

    override suspend fun getById(id: Long): User? = userService.getById(id)
    override suspend fun add(user: User): User = userService.add(user)
    override suspend fun update(user: User): User {
        val u = userService.update(user)
        refreshUsers()
        return u
    }

    override suspend fun delete(id: Long) {
        userService.delete(id)
        refreshUsers()
    }

    override suspend fun search(query: String): List<User> {
        val users = userService.search(query)
        _users.value = users
        return users
    }

    override suspend fun toggleFavorite(id: Long) {
        userService.toggleFavorite(id)
        refreshUsers()
    }

    override suspend fun getFavorites(): List<User> = userService.getFavorites()
    override suspend fun sortBy(criteria: SortCriteria): List<User> {
        val users = userService.sortBy(criteria)
        _users.value = users
        return users
    }
}