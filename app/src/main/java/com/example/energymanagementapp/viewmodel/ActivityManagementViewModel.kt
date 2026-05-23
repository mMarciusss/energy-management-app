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

    // veiklų sąrašas, rodomas veiklų valdymo ekrane
    var activities by mutableStateOf<List<ActivityEntity>>(emptyList())
        private set

    init {
        // užkraunamas veiklų sąrašas paleidimo metu
        loadActivities()
    }

    // visų veiklų užkrovimas
    private fun loadActivities(){
        viewModelScope.launch {
            activities = activityRepository.getActivityList()
        }
    }

    // veiklų sąrašo atnaujinimas
    fun refreshActivities(){
        loadActivities()
    }

    // veiklos išsaugojimas DB
    fun addActivity(name: String, energyCost: Int) {
        if (energyCost !in 1..5) return

        viewModelScope.launch {
            activityRepository.saveActivity(name, energyCost)
            loadActivities()
        }
    }

    // veiklos pašalinimas iš DB
    fun deleteActivity(activity: ActivityEntity) {
        viewModelScope.launch {
            activityRepository.deleteActivity(activity)
            loadActivities()
        }
    }
}