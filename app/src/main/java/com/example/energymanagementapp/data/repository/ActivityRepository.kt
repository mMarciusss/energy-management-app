package com.example.energymanagementapp.data.repository

import com.example.energymanagementapp.data.local.dao.ActivityDao
import com.example.energymanagementapp.data.local.entities.ActivityEntity

class ActivityRepository (
    private val activityDao: ActivityDao
){
    suspend fun saveActivity(name: String, energyCost: Int){
        val activity = ActivityEntity(
            name = name,
            energyCost = energyCost
        )
        activityDao.insertOrUpdateActivity(activity)
    }

    suspend fun deleteActivity(activity: ActivityEntity){
        activityDao.deleteActivity(activity)
    }

    suspend fun seedActivitiesIfEmpty(onDone: () -> Unit) {
        val existing = getActivityList()

        if(existing.isNotEmpty()) return

        val presetActivities = listOf(
            "Workout" to 3,
            "Reading" to 1,
            "Coding" to 2,
            "Gaming" to 2,
            "Walking" to 1
        )

        presetActivities.forEach {
            saveActivity(
                name = it.first,
                energyCost = it.second
            )
        }

        onDone()
    }

    suspend fun getActivityList(): List<ActivityEntity>{
        return activityDao.getActivities()
    }
}