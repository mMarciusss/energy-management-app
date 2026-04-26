package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DaySummaryViewModel (
    private val planActivityRepository: PlanActivityRepository
) : ViewModel() {

    var activities by mutableStateOf<List<PlanActivityWithBreak>>(emptyList())
        private set

    var totalEnergyUsed by mutableStateOf(0)
        private set

    var totalRestTimeMinutes by mutableStateOf(0)
        private set

    fun loadSummary(){
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val list = planActivityRepository.getPlanActivitiesWithBreaks(today)

            activities = list

            totalEnergyUsed = list
                .filter { it.isCompleted }
                .sumOf { it.energyCost }

            totalRestTimeMinutes = list.sumOf { activity ->
                val start = activity.startTime ?: 0L
                val end = activity.endTime ?: 0L

                if(start > 0L && end > 0L && end > start){
                    ((end - start) / 1000 / 60).toInt()
                } else {
                    0
                }
            }
        }
    }
}