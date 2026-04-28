package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

data class DayStatus(
    val date: LocalDate,
    val status: Status
)

enum class Status {
    COMPLETED,
    PARTIAL,
    NOT_COMPLETED
}

class PastDaysViewModel (
    private val repository: PlanActivityRepository
) : ViewModel() {

    var dayStatuses by mutableStateOf<List<DayStatus>>(emptyList())
        private set

    fun loadDayStatuses() {
        viewModelScope.launch {
            val dates = repository.getAllDates()

            val result = dates.map { date ->
                val planActivities = repository.getPlanActivitiesWithBreaks(date)

                val total = planActivities.size
                val completed = planActivities.count {it.isCompleted}

                val status = when {
                    total == 0 -> Status.NOT_COMPLETED
                    completed == total -> Status.COMPLETED
                    completed > 0 -> Status.PARTIAL
                    else -> Status.NOT_COMPLETED
                }

                DayStatus(LocalDate.parse(date), status)

            }

            dayStatuses = result
        }
    }
}