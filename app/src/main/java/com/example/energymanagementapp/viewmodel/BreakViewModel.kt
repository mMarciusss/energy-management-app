package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.data.model.PlanActivityWithDetails
import com.example.energymanagementapp.data.repository.BreakRepository
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BreakViewModel (
    private val planActivityRepository: PlanActivityRepository,
    private val breakRepository: BreakRepository,
) : ViewModel() {

    var planActivities by mutableStateOf<List<PlanActivityWithBreak>>(emptyList())

    var breakDuration by mutableIntStateOf(30)
        private set

    var remainingEnergy by mutableStateOf(0)
        private set

    var totalEnergy by mutableStateOf(0)
        private set

    init {
        loadPlanActivities()
    }

    private fun loadPlanActivities() {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            planActivities = planActivityRepository.getPlanActivitiesWithBreaks(today)
        }
    }

    fun reloadPlanActivities() {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val list = planActivityRepository.getPlanActivitiesWithBreaks(today)

            val usedEnergy = list
                .filter { it.isCompleted}
                .sumOf { it.energyCost }

            remainingEnergy = totalEnergy - usedEnergy

            planActivities = list
        }
    }

    fun setEnergy(energy: Int){
        totalEnergy = energy
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
            )
        }
    }

    fun loadBreak(planActivityId: Int){
        viewModelScope.launch {
            val existing = breakRepository.getBreak(planActivityId)
            breakDuration = existing?.durationMinutes ?: 30
        }
    }

    fun toggleComplete(id: Int){
        viewModelScope.launch {
            val current = planActivities.find {it.id == id}

            if(current != null){
                planActivityRepository.toggleComplete(id, !current.isCompleted)
                reloadPlanActivities()
            }
        }
    }

    fun completeActivities(ids: List<Int>, onDone: (Int?) -> Unit) {
        viewModelScope.launch {
            var firstBreakActivityId: Int? = null

            ids.forEach { id ->
                val activity = planActivities.find {it.id == id}

                if(activity != null) {
                    planActivityRepository.toggleComplete(id, true)

                    if(activity.breakDuration != null && firstBreakActivityId == null) {
                        firstBreakActivityId = id
                    }
                }
            }
            reloadPlanActivities()
            onDone(firstBreakActivityId)
        }
    }
}