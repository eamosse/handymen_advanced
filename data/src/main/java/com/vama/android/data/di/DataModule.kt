package com.vama.android.data.di

import com.vama.android.data.api.InMemoryUserService
import com.vama.android.data.api.UserService
import com.vama.android.data.repositories.UserRepository
import com.vama.android.data.repositories.UserRepositoryImpl

object DataModule {
    fun repository(): UserRepository {
        return UserRepositoryImpl.instance()
    }

    fun provideUserService(): UserService {
        return InMemoryUserService()
    }
}