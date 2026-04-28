package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.core.state.PlanState
import com.example.energymanagementapp.data.local.entities.PlanEntity
import com.example.energymanagementapp.data.repository.PlanRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlanViewModel (
    private val planRepository: PlanRepository
) : ViewModel() {

    var isConfirmed by mutableStateOf(false)
        private set

    var planEndTime by mutableStateOf("20:00")
        private set

    var isExpired by mutableStateOf(false)
        private set

    var planState by mutableStateOf(PlanState.NOT_STARTED)
        private set

    init {
        loadPlan()
    }

    private fun loadPlan(){
        viewModelScope.launch {
            val today = getToday()
            val now = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            val plan = planRepository.getPlan(today)

            if(plan == null){
                planState = PlanState.NOT_STARTED
                return@launch
            }

            isConfirmed = plan.isConfirmed == true
            planEndTime = plan.endTime
            isExpired = planEndTime < now

            planState = when {
                !isConfirmed -> PlanState.CREATING
                isConfirmed && isExpired -> PlanState.COMPLETED
                isConfirmed -> PlanState.CONFIRMED
                else -> PlanState.NOT_STARTED
            }
        }
    }

    private fun getToday(): String{
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
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

    fun resetPlan() {
        viewModelScope.launch {
            val today = getToday()

            planRepository.deletePlan(today)

            isConfirmed = false
            isExpired = false
            planEndTime = "20:00"

            planState = PlanState.NOT_STARTED
        }
    }

    fun setEndTime(endTime: String) {
        viewModelScope.launch {
            val today = getToday()
            planRepository.updateEndTime(today, endTime)

            planEndTime = endTime
        }
    }
}