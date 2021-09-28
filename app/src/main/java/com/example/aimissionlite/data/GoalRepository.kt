package com.example.aimissionlite.data

import androidx.annotation.CheckResult
import androidx.annotation.WorkerThread
import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Status
import kotlinx.coroutines.flow.Flow

class GoalRepository(private val goalDao: IGoalDao) {
    val allGoals: Flow<List<Goal>> = goalDao.getGoals()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(goal: Goal) {
        /**
        First we map the domain model to data before we use the dao to write the new goal into db.
         */
        goalDao.insert(goal)
    }

    suspend fun getGoal(id: Int): Goal = goalDao.getGoal(id)

    @WorkerThread
    @CheckResult
    suspend fun deleteAll(): Boolean {
        return try {
            val result = goalDao.deleteAll()
            println("!! success on deleting all goals? details: $result")
            true
        } catch (error: Throwable) {
            println("!! error while deleting all goals. details: ${error.message}")
            false
        }
    }

    @WorkerThread
    suspend fun updateStatus(id: Int, status: Status) {
        goalDao.updateStatus(
            id = id,
            status = status.toStatusData()
        )
    }

    @WorkerThread
    @CheckResult
    suspend fun deleteGoal(goal: Goal): Boolean {
        return try {
            goalDao.deleteGoal(goal)
            true
        } catch (exception: Exception) {
            false
        }
    }
}