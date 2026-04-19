package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.data.local.entities.ActivityEntity
import com.example.energymanagementapp.data.repository.ActivityRepository
import kotlinx.coroutines.launch

class ActivitySelectionModel (
    private val activityRepository: ActivityRepository
) : ViewModel() {

    var activities by mutableStateOf<List<ActivityEntity>>(emptyList())
        private set

    init {
        loadActivities()
    }

    private fun loadActivities() {
        viewModelScope.launch {
            var list = activityRepository.getActivityList()

            if (list.isEmpty()) {
                seedActivities()
                list = activityRepository.getActivityList()
            }

            activities = list
        }
    }

    private suspend fun seedActivities() {
        val presetActivities = listOf(
            "Workout" to 3,
            "Reading" to 1,
            "Coding" to 2,
            "Gaming" to 2,
            "Walking" to 1
        )

        presetActivities.forEach {
            activityRepository.saveActivity(
                name = it.first,
                energyCost = it.second
            )
        }
    }
}