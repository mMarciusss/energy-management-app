package com.example.energymanagementapp.data.repository

import com.example.energymanagementapp.data.local.dao.BreakDao
import com.example.energymanagementapp.data.local.entities.BreakEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BreakRepository (
    private val breakDao: BreakDao
) {
    suspend fun saveBreak(planActivityId: Int, durationMinutes: Int, startTime: Long, endTime: Long){
        val existing = breakDao.getBreakByPlanActivity(planActivityId)

        if(existing == null){
            breakDao.insertBreak(
                BreakEntity(
                    planActivityId = planActivityId,
                    durationMinutes = durationMinutes,
                    isCompleted = false,
                    startTime = startTime,
                    endTime = endTime
                )
            )
        } else {
            breakDao.updateBreak(
                existing.copy(
                    durationMinutes = durationMinutes,
                    startTime = startTime,
                    endTime = endTime
                )
            )
        }
    }

    suspend fun getBreakList(planActivityId: Int): List<BreakEntity>{
        return breakDao.getBreaksByPlanActivity(planActivityId)
    }

    suspend fun getBreak(planActivityId: Int): BreakEntity?{
        return breakDao.getBreakByPlanActivity(planActivityId)
    }
}