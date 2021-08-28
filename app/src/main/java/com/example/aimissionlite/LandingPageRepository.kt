package com.example.aimissionlite

import android.content.Context
import com.example.aimissionlite.data.GoalRepository
import com.example.aimissionlite.data.LandingPageLocalDataSource

class LandingPageRepository(context: Context) {
    private val localDataSource = LandingPageLocalDataSource(context)

    fun getDeleteGoalsOnStartup() =
        localDataSource.getDeleteGoalsOnStartup() // todo use common local data source for getting information

    suspend fun deleteAllGoals(repository: GoalRepository) = repository.deleteAll()
}