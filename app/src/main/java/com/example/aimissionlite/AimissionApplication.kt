package com.example.aimissionlite

import android.app.Application
import com.example.aimissionlite.data.GoalRepository
import com.example.aimissionlite.data.GoalRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AimissionApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy {
        GoalRoomDatabase.getDatabase(
            context = this,
            scope = applicationScope
        )
    }

    val repository by lazy { GoalRepository(database.goalDao()) }
}