package com.example.energymanagementapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breaks")
data class BreakEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val planActivityId: Int,

    val durationMinutes: Int = 0,
    val isCompleted: Boolean = false,
    val startTime: String = "",
    val endTime: String = ""
)