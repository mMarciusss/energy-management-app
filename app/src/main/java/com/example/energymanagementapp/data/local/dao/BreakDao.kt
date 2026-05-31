package com.example.energymanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.energymanagementapp.data.local.entities.BreakEntity

@Dao
interface BreakDao {
    // įrašomas pertraukos įrašas
    @Insert
    suspend fun insertBreak(activityBreak: BreakEntity)

    // pertraukos atnaujinimas
    @Update
    suspend fun updateBreak(activityBreak: BreakEntity)

    // pertraukos ištrynimas
    @Delete
    suspend fun deleteBreak(activityBreak: BreakEntity)

    // pertraukų ištrynimas
    @Query("""
        DELETE FROM breaks
        WHERE planActivityId IN (
            SELECT id FROM plan_activities
            WHERE planDate = :date
        )
    """)
    suspend fun deleteBreaksByDate(date: String)

    // pertraukų nuskaitymas pagal veiklos ID
    @Query("SELECT * FROM breaks WHERE planActivityId = :planActivityId")
    suspend fun getBreaksByPlanActivity(planActivityId: Int): List<BreakEntity>

    // pertraukos nuskaitymas pagal plano veiklą
    @Query("SELECT * FROM breaks WHERE planActivityId = :id LIMIT 1")
    suspend fun getBreakByPlanActivity(id: Int): BreakEntity?
}