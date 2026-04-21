package com.example.energymanagementapp.data.model

data class PlanActivityWithBreak (
    val id: Int,
    val activityName: String,
    val energyCost: Int,

    val breakDuration: Int?
)