package com.example.aimissionlite.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class LandingPageLocalDataSource(val context: Context) {
    /**
     * TODO
     * At the moment we use functionality here to get data store information on startup.
     * But there are operations which has to be read out here as well as on other fragments like the
     * Settings Fragment. Therefore it is not very good to have a SettingsLocalDataSource and a
     * LandingpageLocalDataSoure that to the same thing. Write a CommonLocalDataSource or something
     * like that and remove the both local data sources. So we can reduce the duplicate code.
     */

    private val Context.dataStore by preferencesDataStore(
        name = USER_SETTINGS_NAME
    )

    fun getDeleteGoalsOnStartup(): Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.DELETE_GOALS_ON_STARTUP] ?: false
        }

    private object PreferencesKeys {
        val DELETE_GOALS_ON_STARTUP = booleanPreferencesKey("delete_goals_on_startup")
    }

    companion object {
        private const val USER_SETTINGS_NAME = "user_settings"
    }
}