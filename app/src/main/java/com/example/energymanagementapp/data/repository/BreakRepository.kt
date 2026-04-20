package com.example.energymanagementapp.data.repository

import com.example.energymanagementapp.data.local.dao.BreakDao
import com.example.energymanagementapp.data.local.entities.BreakEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BreakRepository (
    private val breakDao: BreakDao
) {
    suspend fun saveBreak(planActivityId: Int, durationMinutes: Int, isCompleted: Boolean, startTime: String, endTime: String){
        val activityBreak = BreakEntity(
            planActivityId = planActivityId,
            durationMinutes = durationMinutes,
            isCompleted = isCompleted,
            startTime = startTime,
            endTime = endTime
        )
        breakDao.insertOrUpdateBreak(activityBreak)
    }

    suspend fun getBreakList(planActivityId: Int): List<BreakEntity>{
        return breakDao.getBreaksByPlanActivity(planActivityId)
    }

    suspend fun startBreak(planActivityId: Int) {
        val now = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        val activityBreak = BreakEntity(
            planActivityId = planActivityId,
            startTime = now
        )

        breakDao.insertOrUpdateBreak(activityBreak)
    }

    suspend fun endBreak(activityBreak: BreakEntity) {
        val now = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        val updated = activityBreak.copy(
            endTime = now,
            isCompleted = true
        )

        breakDao.insertOrUpdateBreak(updated)
    }
}