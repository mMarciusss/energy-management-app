package com.example.energymanagementapp.data.repository

import com.example.energymanagementapp.data.local.dao.PlanActivityDao
import com.example.energymanagementapp.data.local.entities.PlanActivityEntity
import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.data.model.PlanActivityWithDetails

class PlanActivityRepository (
    val planActivityDao: PlanActivityDao
) {
    suspend fun savePlanActivity(planDate: String, activityId: Int, activityName: String, energyCost: Int, isCompleted: Boolean, completionTime: String?){
        val planActivity = PlanActivityEntity(
            planDate = planDate,
            activityId = activityId,
            activityName = activityName,
            energyCost = energyCost,
            isCompleted = isCompleted,
            completionTime = completionTime
        )
        planActivityDao.insertOrUpdate(planActivity)
    }

    suspend fun getAllDates(): List<String>{
        return planActivityDao.getAllDates()
    }

    suspend fun getPlanActivities(planDate: String): List<PlanActivityEntity>{
        return planActivityDao.getPlanActivitiesByDate(planDate)
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

    suspend fun completeActivity(id: Int, completionTime: String){
        planActivityDao.updateCompletion(id, true, completionTime)
    }
}