package com.example.aimissionlite.data.settings.repository

import android.content.Context
import com.example.aimissionlite.data.SettingsLocalDataSource
import java.util.concurrent.Flow

class SettingsRepository(context: Context) {
    private val localDataSource = SettingsLocalDataSource(context)

    suspend fun setDeleteGoalsOnStartup(enabled: Boolean) {
        localDataSource.setDeleteGoalsOnStartup(enabled)
    }

    fun getDeleteGoalsOnStartup() = localDataSource.getDeleteGoalsOnStartup()
}