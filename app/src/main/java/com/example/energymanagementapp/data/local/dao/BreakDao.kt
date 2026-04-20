package com.example.energymanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.energymanagementapp.data.local.entities.BreakEntity

@Dao
interface BreakDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateBreak(activityBreak: BreakEntity)

    @Query("SELECT * FROM breaks WHERE planActivityId = :planActivityId")
    suspend fun getBreaksByPlanActivity(planActivityId: Int): List<BreakEntity>
}