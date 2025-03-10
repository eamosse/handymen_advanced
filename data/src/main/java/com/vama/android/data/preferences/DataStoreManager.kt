package com.vama.android.data.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    fun isDatabaseModeFlow(): Flow<Boolean> = callbackFlow {
        trySend(prefs.getBoolean(PREF_MODE_KEY, true))

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == PREF_MODE_KEY) {
                trySend(prefs.getBoolean(PREF_MODE_KEY, true))
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    fun toggleDatabaseMode() {
        prefs.edit().putBoolean(PREF_MODE_KEY, !isDatabaseMode()).apply()
    }
}