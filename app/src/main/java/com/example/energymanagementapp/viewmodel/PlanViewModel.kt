package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.core.state.PlanState
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

    // paskutinė užkrauta data, naudojama dienos pasikeitimui aptikti
    private var lastLoadedDate: String? = null

    // ar planas jau patvirtintas
    var isConfirmed by mutableStateOf(false)
        private set

    // plano pabaigos laikas
    var planEndTime by mutableStateOf("20:00")
        private set

    // ar plano laikas jau pasibaigęs
    var isExpired by mutableStateOf(false)
        private set

    // dabartinė plano būsena
    var planState by mutableStateOf(PlanState.NOT_STARTED)
        private set

    // ar visos plano veiklos atliktos
    var isAllCompleted by mutableStateOf(false)
        private set

    // ar per vėlu pradėti naują planą
    var isTooLateToStart by mutableStateOf(false)
        private set

    init {
        // užkraunamas planas paleidimo metu
        loadPlan()
    }

    // plano užkrovimas
    private fun loadPlan() {
        viewModelScope.launch {
            val today = getToday()

            if (lastLoadedDate != null && lastLoadedDate != today) {
                resetLocalState()
            }
            lastLoadedDate = today

            val now = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            val plan = planRepository.getPlan(today)

            val hour = SimpleDateFormat("HH", Locale.getDefault()).format(Date()).toInt()
            isTooLateToStart = hour >= 21

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

    // išvaloma lokali plano būsena
    private fun resetLocalState() {
        isConfirmed = false
        isExpired = false
        isAllCompleted = false
        planEndTime = "20:00"
        planState = PlanState.NOT_STARTED
    }

    // pradedamas plano kūrimas
    fun startCreatingPlan() {
        if (planState == PlanState.NOT_STARTED) {
            planState = PlanState.CREATING
        }
    }

    // plano patvirtinimas
    fun confirmPlan(onDone: () -> Unit) {
        viewModelScope.launch {
            val today = getToday()
            planRepository.confirmPlan(today)
            isConfirmed = true
            planState = PlanState.CONFIRMED
            onDone()
        }
    }

    // plano atšaukimas ir duomenų ištrynimas
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

    // plano perkrovimas
    fun reloadPlan(){
        loadPlan()
    }

    // šiandienos datos grąžinimas
    fun getToday(): String{
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    // plano pabaigos laiko nustatymas
    fun setEndTime(endTime: String) {
        viewModelScope.launch {
            val today = getToday()
            planRepository.updateEndTime(today, endTime)

            planEndTime = endTime
        }
    }
}
