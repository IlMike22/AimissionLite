package com.example.aimissionlite.models

import com.example.aimissionlite.data.GoalRepository

interface ILandingPageUseCase {
    suspend fun deleteAllGoals(): Boolean
}