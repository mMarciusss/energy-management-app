package com.example.energymanagementapp.data.repository

import com.example.energymanagementapp.data.local.dao.PlanDao
import com.example.energymanagementapp.data.local.entities.PlanEntity

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

    suspend fun getEnergy(date: String): Int {
        val plan = planDao.getPlanByDate(date)
        return plan?.energyLevel ?: 0
    }
}