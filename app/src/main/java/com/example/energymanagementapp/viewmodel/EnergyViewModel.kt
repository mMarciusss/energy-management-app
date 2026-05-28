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

    // minimalus leidžiamas energijos kiekis
    private val MIN_ENERGY = 3

    // dabartinė pasirinkta energija
    var energy by mutableStateOf(5)
        private set

    // ar energija jau išsaugota šiandien
    var isEnergySet by mutableStateOf(false)
        private set

    init{
        // užkraunama šiandienos energija paleidimo metu
        loadTodayEnergy()
    }

    // užkraunama šiandienos energija iš DB
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

    // rankinis energijos atnaujinimas
    fun reloadEnergy(){
        loadTodayEnergy()
    }

    // energijos padidinimas
    fun increaseEnergy(){
        if (energy < 20){
           energy++
        }
    }

    // energijos sumažinimas
    fun decreaseEnergy(){
        if (energy > MIN_ENERGY){
            energy--
        }
    }

    // energijos išsaugojimas DB
    fun saveEnergy(onDone: () -> Unit){
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            repository.saveEnergy(today, energy)
            isEnergySet = true
            onDone()
        }
    }
}