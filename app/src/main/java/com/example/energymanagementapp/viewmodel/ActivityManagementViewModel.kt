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

    fun addActivity(name: String, energyCost: Int) {
        viewModelScope.launch {
            activityRepository.saveActivity(name, energyCost)
        }
    }

    fun deleteActivity(id: Int) {
        viewModelScope.launch {
            activityRepository.deleteActivity(id)
        }
    }
}