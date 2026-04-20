package com.example.energymanagementapp.data.local.entities

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity("plan_activities")
data class PlanActivityEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val planDate: String,
    val activityId: Int,

    val isCompleted: Boolean = false,
    val completionTime: String? = null
)