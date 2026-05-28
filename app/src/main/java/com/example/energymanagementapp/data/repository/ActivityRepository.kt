package com.example.energymanagementapp.data.repository

import com.example.energymanagementapp.data.local.dao.ActivityDao
import com.example.energymanagementapp.data.local.entities.ActivityEntity
import com.example.energymanagementapp.ui.localization.AppLanguage

class ActivityRepository (
    private val activityDao: ActivityDao
){
    // veiklos įrašymas į DB
    suspend fun saveActivity(name: String, energyCost: Int){
        val activity = ActivityEntity(
            name = name,
            energyCost = energyCost
        )
        activityDao.insertOrUpdateActivity(activity)
    }

    // visų preset veiklų įrašymas į DB tik vieną kartą
    suspend fun persistPresetActivities(language: AppLanguage) {
        val existing = getActivityList()

        val allPresetNames = getPresetActivities(AppLanguage.LT).map { it.name } +
                getPresetActivities(AppLanguage.EN).map { it.name }

        val presetsAlreadySeeded = existing.any { activity ->
            allPresetNames.contains(activity.name)
        }

        if (presetsAlreadySeeded) return

        val presets = getPresetActivities(language)

        presets.forEach {
            saveActivity(
                name = it.name,
                energyCost = it.energyCost
            )
        }
    }

    // veiklos ištrynimas iš DB
    suspend fun deleteActivity(activity: ActivityEntity){
        activityDao.deleteActivity(activity)
    }

    // pradinių veiklų grąžinimas pagal pasirinktą kalbą, bet jų neįrašant į DB
    fun getPresetActivities(language: AppLanguage): List<ActivityEntity> {
        return when (language) {
            AppLanguage.LT -> listOf(
                ActivityEntity(id = -1, name = "Sportas", energyCost = 3),
                ActivityEntity(id = -2, name = "Skaitymas", energyCost = 1),
                ActivityEntity(id = -3, name = "Programavimas", energyCost = 2),
                ActivityEntity(id = -4, name = "Žaidimai", energyCost = 2),
                ActivityEntity(id = -5, name = "Pasivaikščiojimas", energyCost = 1)
            )

            AppLanguage.EN -> listOf(
                ActivityEntity(id = -1, name = "Workout", energyCost = 3),
                ActivityEntity(id = -2, name = "Reading", energyCost = 1),
                ActivityEntity(id = -3, name = "Coding", energyCost = 2),
                ActivityEntity(id = -4, name = "Gaming", energyCost = 2),
                ActivityEntity(id = -5, name = "Walking", energyCost = 1)
            )
        }
    }

    // veiklų nuskaitymas iš DB
    suspend fun getActivityList(): List<ActivityEntity>{
        return activityDao.getActivities()
    }
}