package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.data.local.entities.ActivityEntity
import com.example.energymanagementapp.data.repository.ActivityRepository
import com.example.energymanagementapp.ui.localization.AppLanguage
import kotlinx.coroutines.launch

class ActivityManagementViewModel (
    private val activityRepository: ActivityRepository
) : ViewModel() {

    // veiklų sąrašas, rodomas veiklų valdymo ekrane
    var activities by mutableStateOf<List<ActivityEntity>>(emptyList())
        private set

    private var currentLanguage = AppLanguage.EN

    init {
        // užkraunamas veiklų sąrašas paleidimo metu
        loadActivities()
    }

    // visų veiklų užkrovimas
    private fun loadActivities() {
        viewModelScope.launch {

            val dbActivities = activityRepository.getActivityList()

            activities = if (dbActivities.isEmpty()) {
                activityRepository.getPresetActivities(currentLanguage)
            } else {
                dbActivities
            }
        }
    }

    fun persistPresetActivities(
        language: AppLanguage,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            activityRepository.persistPresetActivities(language)
            loadActivities()
            onDone()
        }
    }

    // veiklų sąrašo atnaujinimas
    fun refreshActivities(language: AppLanguage = currentLanguage) {
        currentLanguage = language
        loadActivities()
    }

    // veiklos išsaugojimas DB
    fun addActivity(name: String, energyCost: Int, language: AppLanguage) {
        if (energyCost !in 1..5) return

        viewModelScope.launch {
            currentLanguage = language

            activityRepository.persistPresetActivities(language)
            activityRepository.saveActivity(name, energyCost)

            loadActivities()
        }
    }

    // veiklos pašalinimas iš DB
    fun deleteActivity(activity: ActivityEntity, language: AppLanguage) {
        viewModelScope.launch {
            currentLanguage = language

            if (activity.id < 0) {
                activityRepository.persistPresetActivities(language)

                val realActivity = activityRepository.getActivityList()
                    .find {
                        it.name == activity.name &&
                                it.energyCost == activity.energyCost
                    }

                if (realActivity != null) {
                    activityRepository.deleteActivity(realActivity)
                }
            } else {
                activityRepository.deleteActivity(activity)
            }

            loadActivities()
        }
    }
}