package com.example.energymanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.energymanagementapp.data.local.entities.PlanActivityEntity
import com.example.energymanagementapp.data.model.PlanActivityWithBreak
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

    @Query("DELETE FROM plan_activities WHERE planDate = :date")
    suspend fun deleteByDate(date: String)

    @Query("""
        DELETE FROM plan_activities 
        WHERE planDate = :date AND activityId = :activityId
    """)
    suspend fun deletePlanActivityByDateAndActivityId(date: String, activityId: Int)

    @Query("""
        SELECT
            pa.id,
            a.name as activityName,
            a.energyCost,
            b.durationMinutes as breakDuration
        FROM plan_activities pa
        INNER JOIN activities a ON pa.activityId = a.id
        LEFT JOIN breaks b ON b.planActivityId = pa.id
        WHERE pa.planDate = :planDate
    """)
    suspend fun getPlanActivitiesWithBreaks(planDate: String): List<PlanActivityWithBreak>
}