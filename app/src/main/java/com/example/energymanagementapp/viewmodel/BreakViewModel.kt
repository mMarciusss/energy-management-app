package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.data.local.entities.ActivityEntity
import com.example.energymanagementapp.data.local.entities.PlanActivityEntity
import com.example.energymanagementapp.data.repository.ActivityRepository
import com.example.energymanagementapp.data.repository.BreakRepository
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BreakViewModel (
    private val planActivityRepository: PlanActivityRepository,
    private val breakRepository: BreakRepository,
    private val activityRepository: ActivityRepository
) : ViewModel() {

    var planActivities by mutableStateOf<List<PlanActivityEntity>>(emptyList())

    var activities by mutableStateOf<List<ActivityEntity>>(emptyList())
    var breakDuration by mutableStateOf<Int>(30)
        private set

    init {
        loadPlanActivities()
        loadActivities()
    }

    private fun loadPlanActivities() {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            planActivities = planActivityRepository.getPlanActivities(today)
        }
    }

    private fun loadActivities() {
        viewModelScope.launch {
            activities = activityRepository.getActivityList()
        }
    }

    fun increaseBreakDuration(){
        if(breakDuration <=175)
            breakDuration += 5
    }

    fun decreaseBreakDuration(){
        if(breakDuration > 5)
            breakDuration -= 5
    }

    fun saveBreak(planActivityId: Int){
        viewModelScope.launch {
            breakRepository.saveBreak(
                planActivityId = planActivityId,
                durationMinutes = breakDuration,
                isCompleted = false,
                startTime = "",
                endTime = ""
            )
        }
    }
}