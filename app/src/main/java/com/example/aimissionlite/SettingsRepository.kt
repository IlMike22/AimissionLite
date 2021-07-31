package com.example.aimissionlite

import android.content.Context
import com.example.aimissionlite.data.SettingsLocalDataSource

class SettingsRepository(context: Context) {
    private val localDataSource = SettingsLocalDataSource(context)

    suspend fun setDeleteGoalsOnStartup(enabled:Boolean) {
        localDataSource.deleteGoalsOnStartup(enabled)
    }
}