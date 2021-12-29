package com.example.aimissionlite.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.aimissionlite.R
import com.example.aimissionlite.data.settings.repository.SettingsRepository
import com.example.aimissionlite.domain.common.repository.IGoalRepository
import com.example.aimissionlite.domain.landing_page.use_case.LandingPageUseCase
import com.example.aimissionlite.domain.settings.repository.ISettingsRepository
import com.example.aimissionlite.models.ILandingPageUseCase
import com.example.aimissionlite.presentation.landing_page.navigation.ILandingPageNavigation
import com.example.aimissionlite.presentation.landing_page.navigation.implementation.LandingPageNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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