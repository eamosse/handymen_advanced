package com.vama.android.data.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val PREF_DATA_SOURCE_KEY = "data_source"
        private const val PREF_SYNC_ENABLED_KEY = "sync_enabled"
    }

    fun getDataSource(): DataSource {
        val sourceOrdinal = prefs.getInt(PREF_DATA_SOURCE_KEY, DataSource.MEMORY.ordinal)
        return DataSource.entries[sourceOrdinal]
    }

    fun setDataSource(source: DataSource) {
        prefs.edit().putInt(PREF_DATA_SOURCE_KEY, source.ordinal).apply()
    }

    fun isDatabaseMode(): Boolean {
        return getDataSource() == DataSource.DATABASE
    }

    fun setDatabaseMode(isEnabled: Boolean) {
        setDataSource(if (isEnabled) DataSource.DATABASE else DataSource.MEMORY)
    }

    fun isOnlineMode(): Boolean {
        return getDataSource() == DataSource.ONLINE
    }

    fun setOnlineMode(isEnabled: Boolean) {
        if (isEnabled) {
            setDataSource(DataSource.ONLINE)
        }
    }

    fun isSyncEnabled(): Boolean {
        return prefs.getBoolean(PREF_SYNC_ENABLED_KEY, false)
    }

    fun setSyncEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(PREF_SYNC_ENABLED_KEY, enabled).apply()
    }
}