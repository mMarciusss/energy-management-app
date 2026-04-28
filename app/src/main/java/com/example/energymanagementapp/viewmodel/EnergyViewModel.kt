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

    var isEnergySet by mutableStateOf(false)
        private set

    init{
        loadTodayEnergy()
    }

    private fun loadTodayEnergy(){
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val plan = repository.getPlan(today)

            if(plan != null){
                energy = plan.energyLevel
                isEnergySet = true
            } else {
                energy = 5
                isEnergySet = false
            }
        }
    }

    fun reloadEnergy(){
        loadTodayEnergy()
    }

    fun increaseEnergy(){
        if (energy < 20){
           energy++
        }
    }

    fun decreaseEnergy(){
        if (energy > 0){
            energy--
        }
    }

    fun saveEnergy(onDone: () -> Unit){
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            repository.saveEnergy(today, energy)
            isEnergySet = true
            onDone()
        }
    }
}