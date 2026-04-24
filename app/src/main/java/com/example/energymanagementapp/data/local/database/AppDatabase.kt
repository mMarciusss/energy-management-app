package com.example.energymanagementapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.energymanagementapp.data.local.dao.ActivityDao
import com.example.energymanagementapp.data.local.dao.BreakDao
import com.example.energymanagementapp.data.local.dao.PlanActivityDao
import com.example.energymanagementapp.data.local.dao.PlanDao
import com.example.energymanagementapp.data.local.entities.ActivityEntity
import com.example.energymanagementapp.data.local.entities.BreakEntity
import com.example.energymanagementapp.data.local.entities.PlanActivityEntity
import com.example.energymanagementapp.data.local.entities.PlanEntity

@Database(
    entities = [PlanEntity::class, ActivityEntity::class, PlanActivityEntity::class, BreakEntity::class],
    version = 9
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun planDao(): PlanDao
    abstract fun activityDao(): ActivityDao
    abstract fun planActivityDao(): PlanActivityDao
    abstract fun breakDao(): BreakDao
}