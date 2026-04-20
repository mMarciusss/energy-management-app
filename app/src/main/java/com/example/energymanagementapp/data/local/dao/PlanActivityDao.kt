package com.example.energymanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.energymanagementapp.data.local.entities.PlanActivityEntity
import com.example.energymanagementapp.data.model.PlanActivityWithDetails

@Dao
interface PlanActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(planActivity: PlanActivityEntity)

    @Query("SELECT * FROM plan_activities WHERE planDate = :planDate")
    suspend fun getPlanActivitiesByDate(planDate: String): List<PlanActivityEntity>

    @Query("""
        SELECT
            pa.id,
            pa.planDate,
            pa.activityId,
            pa.isCompleted,
            pa.completionTime,
            a.name as activityName,
            a.energyCost
        FROM plan_activities pa
        INNER JOIN activities a
        ON pa.activityId = a.id
        WHERE pa.planDate = :planDate
    """)
    suspend fun getPlanActivitiesWithDetails(planDate: String): List<PlanActivityWithDetails>
}