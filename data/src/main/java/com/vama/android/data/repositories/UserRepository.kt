package com.vama.android.data.repositories

import com.vama.android.data.api.UserService
import com.vama.android.data.di.DataModule
import com.vama.android.data.model.User

interface UserRepository {
    fun getAll(): List<User>
}

internal class UserRepositoryImpl private constructor() : UserRepository {

    private val service: UserService = DataModule.provideUserService()

    override fun getAll(): List<User> {
        return service.getAll()
    }

    companion object {
        private var instance: UserRepository? = null

        fun instance(): UserRepository {
            return instance!!
        }
    }
}