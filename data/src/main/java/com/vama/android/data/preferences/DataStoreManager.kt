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
        private const val PREF_MODE_KEY = "isDatabaseMode"
    }

    fun isDatabaseMode(): Boolean {
        return prefs.getBoolean(PREF_MODE_KEY, true)
    }

    fun setDatabaseMode(isEnabled: Boolean) {
        prefs.edit().putBoolean(PREF_MODE_KEY, isEnabled).apply()
    }
}