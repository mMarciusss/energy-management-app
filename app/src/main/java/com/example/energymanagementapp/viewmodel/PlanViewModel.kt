package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.core.state.PlanState
import com.example.energymanagementapp.data.local.entities.PlanEntity
import com.example.energymanagementapp.data.repository.BreakRepository
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import com.example.energymanagementapp.data.repository.PlanRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlanViewModel (
    private val planRepository: PlanRepository,
    private val breakRepository: BreakRepository,
    private val planActivityRepository: PlanActivityRepository
) : ViewModel() {

    var isConfirmed by mutableStateOf(false)
        private set

    var planEndTime by mutableStateOf("20:00")
        private set

    var isExpired by mutableStateOf(false)
        private set

    var planState by mutableStateOf(PlanState.NOT_STARTED)
        private set

    var isAllCompleted by mutableStateOf(false)
        private set

    init {
        loadPlan()
    }

    private fun loadPlan() {
        viewModelScope.launch {
            val today = getToday()
            val now = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            val plan = planRepository.getPlan(today)

            if (plan == null) {
                planState = PlanState.NOT_STARTED
                return@launch
            }

            val activities = planActivityRepository.getPlanActivitiesWithBreaks(today)

            val allCompleted = activities.isNotEmpty() && activities.all { it.isCompleted }
            val expired = plan.endTime < now

            isConfirmed = plan.isConfirmed == true
            planEndTime = plan.endTime
            isExpired = expired
            isAllCompleted = allCompleted

            planState = when {
                allCompleted || expired -> PlanState.COMPLETED
                isConfirmed -> PlanState.CONFIRMED
                else -> PlanState.CREATING
            }
        }
    }

    fun confirmPlan(onDone: () -> Unit) {
        viewModelScope.launch {
            val today = getToday()
            planRepository.confirmPlan(today)
            isConfirmed = true
            planState = PlanState.CONFIRMED
            onDone()
        }
    }

    fun resetPlan(onDone: () -> Unit) {
        viewModelScope.launch {
            val today = getToday()

            planRepository.deletePlan(today)
            breakRepository.deleteBreaksByDate(today)
            planActivityRepository.deletePlanActivitiesByDate(today)

            isConfirmed = false
            isExpired = false
            planEndTime = "20:00"

            planState = PlanState.NOT_STARTED

            onDone()
        }
    }

    private fun getToday(): String{
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun setEndTime(endTime: String) {
        viewModelScope.launch {
            val today = getToday()
            planRepository.updateEndTime(today, endTime)

            planEndTime = endTime
        }
    }
}
