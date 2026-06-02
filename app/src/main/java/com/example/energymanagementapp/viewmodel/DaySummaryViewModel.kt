package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import com.example.energymanagementapp.data.repository.PlanRepository
import kotlinx.coroutines.launch

class DaySummaryViewModel (
    private val planActivityRepository: PlanActivityRepository,
    private val planRepository: PlanRepository
) : ViewModel() {

    // dienos veiklų sąrašas su papildoma informacija
    var activities by mutableStateOf<List<PlanActivityWithBreak>>(emptyList())
        private set

    // bendra sunaudota energija
    var totalEnergyUsed by mutableStateOf(0)
        private set

    // bendras energijos skaičius
    var totalEnergy by mutableStateOf(0)
        private set

    // bendras poilsio laikas minutėmis
    var totalRestTimeMinutes by mutableStateOf(0)
        private set

    // užkraunama dienos veiklų ataskaita
    fun loadSummary(date: String){
        viewModelScope.launch {
            val list = planActivityRepository.getPlanActivitiesWithBreaks(date)

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

            val plan = planRepository.getPlan(date)
            totalEnergy = plan?.energyLevel ?: 0
        }
    }
}