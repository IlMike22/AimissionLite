package com.example.aimissionlite.data

import android.content.Context
import androidx.room.CoroutinesRoom
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aimissionlite.models.Goal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Goal::class], version = 2, exportSchema = false)
abstract class GoalRoomDatabase : RoomDatabase() {
    abstract fun goalDao(): IGoalDao

    private class GoalDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.goalDao())
                }
            }
        }

        suspend fun populateDatabase(goalDao: IGoalDao) {
            goalDao.deleteAll()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: GoalRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): GoalRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GoalRoomDatabase::class.java,
                    "goal_database"
                ).addCallback(GoalDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}