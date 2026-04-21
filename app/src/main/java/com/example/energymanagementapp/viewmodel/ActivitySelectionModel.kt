package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.data.local.entities.ActivityEntity
import com.example.energymanagementapp.data.repository.ActivityRepository
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ActivitySelectionModel (
    private val activityRepository: ActivityRepository,
    private val planActivityRepository: PlanActivityRepository,
    initialEnergy: Int
) : ViewModel() {

    var activities by mutableStateOf<List<ActivityEntity>>(emptyList())
        private set

    var selectedActivities = mutableStateListOf<Int>()

    var remainingEnergy by mutableIntStateOf(0)
        private set

    init {
        loadActivities()
        remainingEnergy = initialEnergy
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

    fun toggleActivity(activity: ActivityEntity){
        if(selectedActivities.contains(activity.id)){
            selectedActivities.remove(activity.id)
            remainingEnergy += activity.energyCost
        } else {
            if(remainingEnergy >= activity.energyCost) {
                selectedActivities.add(activity.id)
                remainingEnergy -= activity.energyCost
            }
        }
    }

    fun savePlanActivities(){
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            selectedActivities.forEach { id ->
                planActivityRepository.savePlanActivity(
                    planDate = today,
                    activityId = id,
                    isCompleted = false,
                    completionTime = null
                )
            }
        }
    }

}