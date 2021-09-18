package com.example.aimissionlite.models.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goal_table")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
    val creationDate: String,
    val changeDate: String,
    val isRepeated: Boolean,
    val genre: Genre,
    val status: Status,
    val priority: Priority
) {
    companion object {
        val EMPTY = Goal(0, "", "", "", "", false, Genre.UNKNOWN, Status.UNKOWN, Priority.UNKNOWN)
    }
}



