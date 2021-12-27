package com.example.aimissionlite.di

import android.content.Context
import com.example.aimissionlite.data.settings.repository.SettingsRepository
import com.example.aimissionlite.domain.settings.repository.ISettingsRepository
import com.example.aimissionlite.domain.settings.use_case.implementation.SettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {
    @Provides
    @Singleton
    fun provideUseCase(repository: ISettingsRepository): SettingsUseCase {
        return SettingsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRepository(@ApplicationContext context: Context): ISettingsRepository {
        return SettingsRepository(context)
    }
}