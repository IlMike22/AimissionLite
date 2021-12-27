package com.example.aimissionlite.domain.settings.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    suspend fun setDeleteGoalsOnStartup(enabled: Boolean)

    fun getDeleteGoalsOnStartup(): Flow<Boolean>
}