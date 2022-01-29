package com.example.aimissionlite.di

import com.example.aimissionlite.data.info.repository.InformationRepository
import com.example.aimissionlite.domain.information.repository.IInformationRepository
import com.example.aimissionlite.domain.information.use_case.IInformationUseCase
import com.example.aimissionlite.domain.information.use_case.implementation.InformationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InformationModule {
    @Provides
    @Singleton
    fun provideUseCase(repository: IInformationRepository): IInformationUseCase {
        return InformationUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRepository(): IInformationRepository {
        return InformationRepository()
    }
}