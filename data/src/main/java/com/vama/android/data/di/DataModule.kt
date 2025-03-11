package com.vama.android.data.di

import android.content.Context
import com.vama.android.data.database.RoomUserService
import com.vama.android.data.services.UserService
import com.vama.android.data.online.api.ApiService
import com.vama.android.data.database.AppDatabase
import com.vama.android.data.utils.DataSource
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
annotation class MemoryUserService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatabaseUserService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteUserService

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
    @DatabaseUserService
    fun provideRoomUserService(database: AppDatabase): UserService {
        return RoomUserService(database)
    }

    @Provides
    @Singleton
    @RemoteUserService
    fun provideOnlineUserService(apiService: ApiService): UserService {
        return com.vama.android.data.online.OnlineUserService(apiService)
    }

    @Provides
    @Singleton
    fun provideUserService(
        dataStoreManager: DataStoreManager,
        @DatabaseUserService databaseService: UserService,
        @RemoteUserService onlineService: UserService
    ): UserService {
        return when (dataStoreManager.getDataSource()) {
            DataSource.DATABASE -> databaseService
            DataSource.ONLINE -> onlineService
        }
    }
}