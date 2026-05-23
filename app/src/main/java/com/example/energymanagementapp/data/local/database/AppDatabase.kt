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

// Room duomenų bazės aprašas
@Database(
    // visos lentelės, kurios bus naudojamos DB
    entities = [PlanEntity::class, ActivityEntity::class, PlanActivityEntity::class, BreakEntity::class],
    version = 11
)
abstract class AppDatabase : RoomDatabase() {
    // DAO prieiga prie planų lentelės
    abstract fun planDao(): PlanDao

    // DAO prieiga prie veiklų lentelės
    abstract fun activityDao(): ActivityDao

    // DAO prieiga prie suplanuotų veiklų lentelės
    abstract fun planActivityDao(): PlanActivityDao

    // DAO prieiga prie pertraukų lentelės
    abstract fun breakDao(): BreakDao
}