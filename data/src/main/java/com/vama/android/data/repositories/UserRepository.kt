package com.vama.android.data.repositories

import com.vama.android.data.api.SortCriteria
import com.vama.android.data.api.UserService
import com.vama.android.data.model.User
import javax.inject.Inject

interface UserRepository {
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

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserRepository {
    override fun getAll(): List<User> = userService.getAll()
    override fun getById(id: Long): User? = userService.getById(id)
    override fun add(user: User): User = userService.add(user)
    override fun update(user: User): User = userService.update(user)
    override fun delete(id: Long) = userService.delete(id)
    override fun search(query: String): List<User> = userService.search(query)
    override fun toggleFavorite(id: Long) = userService.toggleFavorite(id)
    override fun getFavorites(): List<User> = userService.getFavorites()
    override fun sortBy(criteria: SortCriteria): List<User> = userService.sortBy(criteria)
}