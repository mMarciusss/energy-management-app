package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.data.repository.PlanRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EnergyViewModel (
    private val repository: PlanRepository
) : ViewModel(){

    var energy by mutableStateOf(5)
        private set

    init{
        loadTodayEnergy()
    }

    private fun loadTodayEnergy(){
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            energy = repository.getEnergy(today)
        }
    }

    fun increaseEnergy(){
        viewModelScope.launch {
            energy++
            val today = System.currentTimeMillis().toString()
            repository.saveEnergy(today, energy)
        }
    }

    fun decreaseEnergy(){
        viewModelScope.launch {
            if (energy <= 0) return@launch

            energy--
            val today = System.currentTimeMillis().toString()
            repository.saveEnergy(today, energy)
        }
    }
}