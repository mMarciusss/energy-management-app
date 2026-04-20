package com.example.energymanagementapp.data.repository

import android.util.Log
import com.example.energymanagementapp.data.local.dao.PlanDao
import com.example.energymanagementapp.data.local.entities.PlanEntity

class PlanRepository (
    private val planDao: PlanDao
) {
    suspend fun saveEnergy(date: String, energyLevel: Int){
        Log.d("DB_TEST", "Saving energy: $energyLevel")
        val plan = PlanEntity(
            date = date,
            energyLevel = energyLevel
        )
        planDao.insertOrUpdatePlan(plan)
    }

    suspend fun getPlan(date: String): PlanEntity? {
        return planDao.getPlanByDate(date)
    }
}