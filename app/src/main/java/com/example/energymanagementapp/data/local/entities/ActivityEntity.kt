package com.example.energymanagementapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("activities")
class ActivityEntity (
    @PrimaryKey
    val id: Int,
    val name: String,
    val energyCost: Int
)