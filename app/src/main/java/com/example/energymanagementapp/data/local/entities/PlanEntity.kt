package com.example.energymanagementapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plans")
data class PlanEntity(
    @PrimaryKey
    val date: String,
    val energyLevel: Int,
    val isConfirmed: Boolean = false,
    val endTime: String = "22:00"
)