package com.example.energymanagementapp.data.model

data class PlanActivityWithDetails (
    val id: Int,
    val planDate: String,
    val activityId: Int,
    val isCompleted: Boolean,
    val completionTime: String?,

    val activityName: String,
    val energyCost: Int
)