package com.example.energymanagementapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.energymanagementapp.data.local.dao.PlanDao
import com.example.energymanagementapp.data.local.entities.PlanEntity

@Database(
    entities = [PlanEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun planDao(): PlanDao
}