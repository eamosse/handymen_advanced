package com.eamosse.android.data.repositories

import com.eamosse.android.data.api.UserService
import com.eamosse.android.data.di.DataModule
import com.eamosse.android.data.model.User

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