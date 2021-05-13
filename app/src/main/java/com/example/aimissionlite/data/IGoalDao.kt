package com.example.aimissionlite.data

import androidx.room.*
import com.example.aimissionlite.models.domain.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface IGoalDao {
    @Query("SELECT * FROM goal_table")
    fun getGoals(): Flow<List<Goal>>

//    @Query("SELECT * FROM goal_table WHERE id = id")
//    fun getGoal(id: Int): Flow<Goal>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(goal: Goal)

    @Query("DELETE FROM goal_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteGoal(goal: Goal)
}