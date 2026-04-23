package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.performInTransactionSuspending
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
                startTime = 0L,
                endTime = 0L
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

    fun completeActivities(ids: List<Int>, onBreakNeeded: (Int?) -> Unit) {
        viewModelScope.launch {

            val selected = planActivities.filter { ids.contains(it.id) }
            val withBreak = selected.filter { it.breakDuration != null }
            val withoutBreak = selected.filter { it.breakDuration == null }

            withoutBreak.forEach {
                planActivityRepository.toggleComplete(it.id, true)
            }

            reloadPlanActivities()

            if (withBreak.isNotEmpty()) {
                onBreakNeeded(withBreak.first().id)
            }
        }
    }

    fun startBreakTimer(planActivityId: Int, onDone: () -> Unit){
        viewModelScope.launch {
            val existing = breakRepository.getBreak(planActivityId)

            if(existing != null){
                val now = System.currentTimeMillis()
                val end = now + existing.durationMinutes * 60 * 1000L

                breakRepository.saveBreak(
                    planActivityId = existing.planActivityId,
                    durationMinutes = existing.durationMinutes,
                    startTime = now,
                    endTime = end
                )
            }

            onDone()
        }
    }

    fun getRunningBreakActivityId(): Int?{
        val now = System.currentTimeMillis()

        return planActivities
            .firstOrNull{ activity ->
                val hasBreakStarted = (activity.startTime ?: 0L) > 0L
                val breakNotfinished = (activity.endTime ?: 0L) > now
                hasBreakStarted && breakNotfinished && !activity.isCompleted
            }
            ?.id
    }

    fun finishBreak(planActivityId: Int){
        viewModelScope.launch {
            val existing = breakRepository.getBreak(planActivityId)

            if(existing != null) {
                breakRepository.saveBreak(
                    planActivityId = existing.planActivityId,
                    durationMinutes = existing.durationMinutes,
                    startTime = existing.startTime,
                    endTime = System.currentTimeMillis()
                )
            }
        }
    }

    fun completeAfterBreak(planActivityId: Int, onDone: () -> Unit){
        viewModelScope.launch {

            val existing = breakRepository.getBreak(planActivityId)

            if(existing != null) {
                breakRepository.saveBreak(
                    planActivityId = existing.planActivityId,
                    durationMinutes = existing.durationMinutes,
                    startTime = existing.startTime,
                    endTime = System.currentTimeMillis()
                )
            }

            planActivityRepository.toggleComplete(planActivityId, true)
            reloadPlanActivities()
            onDone()
        }
    }
}