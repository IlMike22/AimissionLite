package com.example.aimissionlite.di

import com.example.aimissionlite.domain.common.repository.IGoalRepository
import com.example.aimissionlite.domain.landing_page.use_case.implementation.LandingPageUseCase
import com.example.aimissionlite.domain.settings.repository.ISettingsRepository
import com.example.aimissionlite.domain.landing_page.use_case.ILandingPageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LandingPageModule {
    @Provides
    @Singleton
    fun provideUseCase(
        goalRepository: IGoalRepository,
        settingsRepository: ISettingsRepository
    ): ILandingPageUseCase =
        LandingPageUseCase(
            goalRepository = goalRepository,
            settingsRepository = settingsRepository
        )
}