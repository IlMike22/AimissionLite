package com.example.aimissionlite.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.aimissionlite.data.SettingsLocalDataSource.PreferencesKeys.DELETE_GOALS_ON_STARTUP
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class SettingsLocalDataSource(val context: Context) {
    private val Context.dataStore by preferencesDataStore(
        name = USER_SETTINGS_NAME
    )

    suspend fun setDeleteGoalsOnStartup(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DELETE_GOALS_ON_STARTUP] = enabled
            println("!!! value was successfully set in datastore.")
        }
    }

    fun getDeleteGoalsOnStartup(): Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            preferences[DELETE_GOALS_ON_STARTUP] ?: false
        }

    private object PreferencesKeys {
        val DELETE_GOALS_ON_STARTUP = booleanPreferencesKey("delete_goals_on_startup")
    }

    companion object {
        private const val USER_SETTINGS_NAME = "user_settings"
    }
}
