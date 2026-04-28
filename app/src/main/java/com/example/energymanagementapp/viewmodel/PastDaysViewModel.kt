package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import kotlinx.coroutines.launch

class PastDaysViewModel (
    private val repository: PlanActivityRepository
) : ViewModel() {

    var dates by mutableStateOf<List<String>>(emptyList())
        private set

    fun loadDates() {
        viewModelScope.launch {
            dates = repository.getAllDates()
        }
    }
}