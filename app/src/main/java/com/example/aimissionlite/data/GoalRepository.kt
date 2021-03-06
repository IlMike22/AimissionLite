package com.example.aimissionlite.data

import androidx.annotation.WorkerThread
import com.example.aimissionlite.models.Goal
import kotlinx.coroutines.flow.Flow

class GoalRepository(private val goalDao: IGoalDao) {
    val allGoals: Flow<List<Goal>> = goalDao.getGoals()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(goal: Goal) {
        goalDao.insert(goal)
    }
}