package com.example.energymanagementapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("plan_activities")
data class PlanActivityEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val planDate: String,
    val activityId: Int,
    val activityName: String,
    val energyCost: Int,

    val isCompleted: Boolean = false,
    val completionTime: String? = null
)