package com.eamosse.android.data.di

import com.eamosse.android.data.api.InMemoryUserService
import com.eamosse.android.data.api.UserService
import com.eamosse.android.data.repositories.UserRepository
import com.eamosse.android.data.repositories.UserRepositoryImpl

object DataModule {
    fun repository(): UserRepository {
        return UserRepositoryImpl.instance()
    }

    fun provideUserService(): UserService {
        return InMemoryUserService()
    }
}