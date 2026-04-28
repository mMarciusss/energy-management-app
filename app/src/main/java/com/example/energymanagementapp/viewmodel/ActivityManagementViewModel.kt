package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.data.local.entities.ActivityEntity
import com.example.energymanagementapp.data.repository.ActivityRepository
import kotlinx.coroutines.launch

class ActivityManagementViewModel (
    private val activityRepository: ActivityRepository
) : ViewModel() {

    var activities by mutableStateOf<List<ActivityEntity>>(emptyList())
        private set

    init {
        loadActivities()
    }

    private fun loadActivities(){
        viewModelScope.launch {
            activities = activityRepository.getActivityList()
        }
    }

    fun refreshActivities(){
        loadActivities()
    }

    fun addActivity(name: String, energyCost: Int) {
        viewModelScope.launch {
            activityRepository.saveActivity(name, energyCost)
            loadActivities()
        }
    }

    fun deleteActivity(activity: ActivityEntity) {
        viewModelScope.launch {
            activityRepository.deleteActivity(activity)
            loadActivities()
        }
    }
}