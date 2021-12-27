package com.example.aimissionlite.di

import com.example.aimissionlite.domain.info.repository.IInfoRepository
import com.example.aimissionlite.data.info.repository.InfoRepository
import com.example.aimissionlite.domain.info.use_case.GetInformationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InfoModule {
    @Provides
    @Singleton
    fun provideUseCase(repository: IInfoRepository): GetInformationUseCase {
        return GetInformationUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRepository(): IInfoRepository {
        return InfoRepository()
    }
}