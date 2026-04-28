package com.example.energymanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.energymanagementapp.data.local.entities.BreakEntity

@Dao
interface BreakDao {
    @Insert
    suspend fun insertBreak(activityBreak: BreakEntity)

    @Update
    suspend fun updateBreak(activityBreak: BreakEntity)

    @Delete
    suspend fun deleteBreak(activityBreak: BreakEntity)

    @Query("""
        DELETE FROM breaks
        WHERE planActivityId IN (
            SELECT id FROM plan_activities
            WHERE planDate = :date
        )
    """)
    suspend fun deleteBreaksByDate(date: String)

    @Query("SELECT * FROM breaks WHERE planActivityId = :planActivityId")
    suspend fun getBreaksByPlanActivity(planActivityId: Int): List<BreakEntity>

    @Query("SELECT * FROM breaks WHERE planActivityId = :id LIMIT 1")
    suspend fun getBreakByPlanActivity(id: Int): BreakEntity?
}