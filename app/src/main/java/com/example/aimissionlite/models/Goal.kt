package com.example.aimissionlite.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "goal_table")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description:String,
//    val creationDate: Date,
//    val changeDate: Date,
//    val isRepeated: Boolean,
//    val genre: Genre,
//    val status: Status,
//    val priority: Priority
)
