package com.example.energymanagementapp.data.repository

import com.example.energymanagementapp.data.local.dao.PlanActivityDao
import com.example.energymanagementapp.data.local.entities.PlanActivityEntity
import java.sql.Date

class PlanActivityRepository (
    val planActivityDao: PlanActivityDao
) {
    suspend fun savePlanActivity(planDate: String, activityId: Int, isCompleted: Boolean, completionTime: String?){
        val planActivity = PlanActivityEntity(
            planDate = planDate,
            activityId = activityId,
            isCompleted = isCompleted,
            completionTime = completionTime
        )
        planActivityDao.insertOrUpdate(planActivity)
    }

    suspend fun getPlanActivities(planDate: String): List<PlanActivityEntity>{
        return planActivityDao.getPlanActivitiesByDate(planDate)
    }
}