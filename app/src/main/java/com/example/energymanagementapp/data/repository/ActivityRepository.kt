package com.example.energymanagementapp.data.repository

import com.example.energymanagementapp.data.local.dao.ActivityDao
import com.example.energymanagementapp.data.local.entities.ActivityEntity

class ActivityRepository (
    private val activityDao: ActivityDao
){
    suspend fun saveActivity(name: String, energyCost: Int){
        var activity = ActivityEntity(
            id = 0,
            name = name,
            energyCost = energyCost
        )
        activityDao.insertOrUpdateActivity(activity)
    }

    suspend fun getActivityList(): List<ActivityEntity>{
        return activityDao.getActivities()
    }

    suspend fun selectActivity(name: String): ActivityEntity?{
        return activityDao.getActivityByName(name)
    }
}