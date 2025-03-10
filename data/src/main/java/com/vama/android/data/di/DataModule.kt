package com.vama.android.data.di

import android.content.Context
import com.vama.android.data.api.InMemoryUserService
import com.vama.android.data.api.RoomUserService
import com.vama.android.data.api.UserService
import com.vama.android.data.database.AppDatabase
import com.vama.android.data.preferences.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Database

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InMemory

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

    @Database
    @Provides
    @Singleton
    fun provideRoomUserService(database: AppDatabase): UserService {
        return RoomUserService(database)
    }

    @InMemory
    @Provides
    @Singleton
    fun provideInMemoryUserService(): UserService {
        return InMemoryUserService()
    }
}