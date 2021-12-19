package com.example.aimissionlite.domain.landing_page.use_case

import com.example.aimissionlite.data.GoalRepository
import com.example.aimissionlite.models.ILandingPageUseCase

class LandingPageUseCase(
    private val repository: GoalRepository
) : ILandingPageUseCase {
    override suspend fun deleteAllGoals(): Boolean = repository.deleteAll()
}