package com.example.energymanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.energymanagementapp.data.local.entities.PlanActivityEntity

@Dao
interface PlanActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(planActivity: PlanActivityEntity)

    @Query("SELECT * FROM plan_activities WHERE planDate = :planDate")
    suspend fun getPlanActivitiesByDate(planDate: String): List<PlanActivityEntity>
}