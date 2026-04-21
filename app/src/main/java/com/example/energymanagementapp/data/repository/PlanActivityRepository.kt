package com.example.energymanagementapp.data.repository

import com.example.energymanagementapp.data.local.dao.PlanActivityDao
import com.example.energymanagementapp.data.local.entities.PlanActivityEntity
import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.data.model.PlanActivityWithDetails

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

    suspend fun getPlanActivitiesWithDetails(planDate: String): List<PlanActivityWithDetails> {
        return planActivityDao.getPlanActivitiesWithDetails(planDate)
    }

    suspend fun deletePlanActivitiesByDate(date: String){
        planActivityDao.deleteByDate(date)
    }

    suspend fun deletePlanActivityByDateAndActivityId(date: String, activityId: Int){
        planActivityDao.deletePlanActivityByDateAndActivityId(date, activityId)
    }

    suspend fun getPlanActivitiesWithBreaks(planDate: String): List<PlanActivityWithBreak>{
        return planActivityDao.getPlanActivitiesWithBreaks(planDate)
    }
}