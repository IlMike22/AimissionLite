package com.example.aimissionlite.data.settings.repository

import android.content.Context
import com.example.aimissionlite.data.SettingsLocalDataSource
import com.example.aimissionlite.domain.settings.repository.ISettingsRepository

class SettingsRepository(context: Context) : ISettingsRepository {
    private val localDataSource = SettingsLocalDataSource(context)

    override suspend fun setDeleteGoalsOnStartup(enabled: Boolean) {
        localDataSource.setDeleteGoalsOnStartup(enabled)
    }

    override fun getDeleteGoalsOnStartup() = localDataSource.getDeleteGoalsOnStartup()
}