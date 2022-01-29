package com.example.aimissionlite.di

import com.example.aimissionlite.data.GoalRoomDatabase
import com.example.aimissionlite.data.common.repository.GoalRepository
import com.example.aimissionlite.domain.common.repository.IGoalRepository
import com.example.aimissionlite.domain.detail.use_case.IDetailUseCase
import com.example.aimissionlite.domain.detail.use_case.implementation.DetailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DetailModule {
    @Provides
    @Singleton
    fun provideUseCase(repository: IGoalRepository): IDetailUseCase {
        return DetailUseCase(repository)
    }
}