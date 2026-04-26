package com.example.energymanagementapp.data.repository

import android.util.Log
import com.example.energymanagementapp.data.local.dao.PlanDao
import com.example.energymanagementapp.data.local.entities.PlanEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlanRepository (
    private val planDao: PlanDao
) {
    suspend fun saveEnergy(date: String, energyLevel: Int){
        val plan = PlanEntity(
            date = date,
            energyLevel = energyLevel
        )
        planDao.insertOrUpdatePlan(plan)
    }

    suspend fun getPlan(date: String): PlanEntity? {
        return planDao.getPlanByDate(date)
    }

    suspend fun confirmPlan(date: String) {
        planDao.confirmPlan(date)
    }

    suspend fun updateEndTime(date: String, endTime: String){
        val existing = planDao.getPlanByDate(date)

        if(existing != null){
            val updated = existing.copy(endTime = endTime)
            planDao.insertOrUpdatePlan(updated)
        }
    }
}