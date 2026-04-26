package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.data.local.entities.PlanEntity
import com.example.energymanagementapp.data.repository.PlanRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlanViewModel (
    private val planRepository: PlanRepository
) : ViewModel() {

    var plan by mutableStateOf<PlanEntity?>(null)
        private set
    var isConfirmed by mutableStateOf(false)
        private set

    init {
        loadPlan()
    }

    private fun loadPlan(){
        viewModelScope.launch {
            val today = getToday()
            val todayPlan = planRepository.getPlan(today)

            plan = todayPlan
            isConfirmed = todayPlan?.isConfirmed == true
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
            onDone()
        }
    }

    fun isPlanExpired(): Boolean{
        val currentPlan = plan ?: return false
        val now = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        return now >= currentPlan.endTime
    }
}