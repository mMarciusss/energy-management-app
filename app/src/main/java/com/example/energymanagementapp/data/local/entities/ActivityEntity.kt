package com.example.energymanagementapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("activities")
data class ActivityEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val energyCost: Int
)