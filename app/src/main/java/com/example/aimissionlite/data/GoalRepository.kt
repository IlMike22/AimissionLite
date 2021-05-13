package com.example.aimissionlite.data

import androidx.annotation.WorkerThread
import com.example.aimissionlite.models.domain.Goal
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

//    fun getGoal(id: Int): Flow<Goal> =
//        goalDao.getGoal(id)

}