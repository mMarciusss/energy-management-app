package com.example.energymanagementapp.data.repository

import com.example.energymanagementapp.data.local.dao.PlanActivityDao
import com.example.energymanagementapp.data.local.entities.PlanActivityEntity
import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.data.model.PlanActivityWithDetails

class PlanActivityRepository (
    val planActivityDao: PlanActivityDao
) {
    // plano veiklos įrašymas į DB
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

    // planų datų nuskaitymas iš DB
    suspend fun getAllDates(): List<String>{
        return planActivityDao.getAllDates()
    }

    // plano veiklų nuskaitymas iš DB
    suspend fun getPlanActivities(planDate: String): List<PlanActivityEntity>{
        return planActivityDao.getPlanActivitiesByDate(planDate)
    }

    // plano veiklų ištrynimas iš DB
    suspend fun deletePlanActivitiesByDate(date: String){
        planActivityDao.deleteByDate(date)
    }

    // plano veiklos ištrynimas iš DB pagal datą ir veiklos ID
    suspend fun deletePlanActivityByDateAndActivityId(date: String, activityId: Int){
        planActivityDao.deletePlanActivityByDateAndActivityId(date, activityId)
    }

    // plano veiklų su papildoma informacija nuskaitymas
    suspend fun getPlanActivitiesWithBreaks(planDate: String): List<PlanActivityWithBreak>{
        return planActivityDao.getPlanActivitiesWithBreaks(planDate)
    }

    // plano veiklos atlikimo pažymėjimas
    suspend fun completeActivity(id: Int, completionTime: String){
        planActivityDao.updateCompletion(id, true, completionTime)
    }
}