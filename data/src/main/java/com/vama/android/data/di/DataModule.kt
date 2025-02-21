package com.vama.android.data.di

import android.content.Context
import com.vama.android.data.api.InMemoryUserService
import com.vama.android.data.api.RoomUserService
import com.vama.android.data.api.UserService
import com.vama.android.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UseDatabaseMode

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
    @UseDatabaseMode
    fun provideUseDatabaseMode(): Boolean {
        // Pour l'instant on retourne toujours true,
        // plus tard on pourra lire depuis les préférences
        return true
    }

    @Provides
    @Singleton
    fun provideUserService(
        database: AppDatabase,
        @UseDatabaseMode useDatabase: Boolean
    ): UserService {
        return if (useDatabase) {
            RoomUserService(database)
        } else {
            InMemoryUserService()
        }
    }
}