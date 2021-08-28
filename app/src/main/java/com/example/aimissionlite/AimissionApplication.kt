package com.example.aimissionlite

import android.app.Application
import com.example.aimissionlite.data.GoalRepository
import com.example.aimissionlite.data.GoalRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AimissionApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy {
        GoalRoomDatabase.getDatabase(
            context = this,
            scope = applicationScope
        )
    }

    val goalRepository by lazy { GoalRepository(database.goalDao()) }
    val settingsRepository by lazy { SettingsRepository(this) }
    val landingPageRepository by lazy { LandingPageRepository(this)}
}