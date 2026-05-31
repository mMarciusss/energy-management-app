package com.example.energymanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.energymanagementapp.data.local.entities.PlanActivityEntity
import com.example.energymanagementapp.data.model.PlanActivityWithBreak

@Dao
interface PlanActivityDao {
    // įrašomas arba atnaujinamas plano veiklos įrašas
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(planActivity: PlanActivityEntity)

    // planų datų nuskaitymas
    @Query("SELECT DISTINCT planDate FROM plan_activities ORDER BY planDate DESC")
    suspend fun getAllDates(): List<String>

    // plano veiklų nuskaitymas pagal datą
    @Query("SELECT * FROM plan_activities WHERE planDate = :planDate")
    suspend fun getPlanActivitiesByDate(planDate: String): List<PlanActivityEntity>

    // plano veiklų ištrynimas pagal datą
    @Query("DELETE FROM plan_activities WHERE planDate = :date")
    suspend fun deleteByDate(date: String)

    // plano veiklos ištrynimas pagal datą ir veiklos ID
    @Query("""
        DELETE FROM plan_activities 
        WHERE planDate = :date AND activityId = :activityId
    """)
    suspend fun deletePlanActivityByDateAndActivityId(date: String, activityId: Int)

    // plano veiklų su papildoma informacija nuskaitymas
    @Query("""
        SELECT
            pa.id,
            pa.planDate,
            pa.activityId,
            pa.activityName,
            pa.energyCost,
            pa.isCompleted,
            pa.completionTime,
            b.durationMinutes as breakDuration,
            b.startTime,
            b.endTime,
            b.isCompleted as breakIsCompleted
        FROM plan_activities pa
        LEFT JOIN breaks b ON b.planActivityId = pa.id
        WHERE pa.planDate = :planDate
    """)
    suspend fun getPlanActivitiesWithBreaks(planDate: String): List<PlanActivityWithBreak>

    // veiklos atlikimo būsenos atnaujinimas
    @Query("""
        UPDATE plan_activities
        SET isCompleted = :isCompleted,
            completionTime = :completionTime
        WHERE id = :id
    """)
    suspend fun updateCompletion(id: Int, isCompleted: Boolean, completionTime: String)
}