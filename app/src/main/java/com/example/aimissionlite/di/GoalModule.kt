package com.example.aimissionlite.di

import android.app.Application
import androidx.room.Room
import com.example.aimissionlite.data.GoalRoomDatabase
import com.example.aimissionlite.data.common.repository.GoalRepository
import com.example.aimissionlite.domain.common.repository.IGoalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoalModule {
    @Provides
    @Singleton
    fun provideGoalRepository(database: GoalRoomDatabase): IGoalRepository {
        return GoalRepository(
            goalDao = database.goalDao()
        )
    }

    @Provides
    @Singleton
    fun provideGoalDatabase(app: Application): GoalRoomDatabase {
        return Room.databaseBuilder(
            app,
            GoalRoomDatabase::class.java,
            "goal_database"
        ).build()
    }
}