package com.vama.android.data.repositories

import com.vama.android.data.api.SortCriteria
import com.vama.android.data.api.UserService
import com.vama.android.data.di.DataModule
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

class UserRepositoryImpl @Inject public constructor() : UserRepository {
    private val service: UserService = DataModule.provideUserService()

    override fun getAll(): List<User> = service.getAll()
    override fun getById(id: Long): User? = service.getById(id)
    override fun add(user: User): User = service.add(user)
    override fun update(user: User): User = service.update(user)
    override fun delete(id: Long) = service.delete(id)
    override fun search(query: String): List<User> = service.search(query)
    override fun toggleFavorite(id: Long) = service.toggleFavorite(id)
    override fun getFavorites(): List<User> = service.getFavorites()
    override fun sortBy(criteria: SortCriteria): List<User> = service.sortBy(criteria)

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun instance(): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepositoryImpl().also { instance = it }
            }
        }
    }
}
