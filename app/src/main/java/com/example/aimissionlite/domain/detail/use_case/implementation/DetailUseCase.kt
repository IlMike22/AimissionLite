package com.example.aimissionlite.domain.detail.use_case.implementation

import com.example.aimissionlite.domain.common.repository.IGoalRepository
import com.example.aimissionlite.domain.detail.use_case.IDetailUseCase
import com.example.aimissionlite.models.domain.Goal

class DetailUseCase(
    private val repository: IGoalRepository
) : IDetailUseCase {
    override suspend fun getGoal(id: Int): Goal =
        repository.getGoal(id)

    override suspend fun updateGoal(goal: Goal): Boolean =
        repository.updateGoal(goal)

    override suspend fun insert(goal: Goal) =
        repository.insert(goal)
}