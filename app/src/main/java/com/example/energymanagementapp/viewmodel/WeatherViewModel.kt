package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel (
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    var temperature by mutableStateOf<Double?>(null)
        private set

    var weatherCode by mutableStateOf<Int?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set


    fun loadWeather() {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                val result = weatherRepository.getCurrentWeather(
                    latitude = 54.6872,
                    longitude = 25.2797
                )

                temperature = result.current.temperature
                weatherCode = result.current.weatherCode
            } catch (e: Exception) {
                errorMessage = "Failed to load weather"
            } finally {
                isLoading = false
            }
        }
    }
}