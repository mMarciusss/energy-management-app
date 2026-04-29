package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.data.repository.WeatherRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class WeatherViewModel (
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    var weatherNow by mutableStateOf<Pair<Double, Int>?>(null)
        private set

    var weatherIn3Hours by mutableStateOf<Pair<Double, Int>?>(null)
        private set

    var weatherEvening by mutableStateOf<Pair<Double, Int>?>(null)
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

                val times = result.hourly.time
                val temperatures = result.hourly.temperature
                val weatherCodes = result.hourly.weatherCode

                val nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val threeHoursPast = (nowHour + 3) % 24

                val nowHourString = nowHour.toString().padStart(2, '0')
                val threeHoursPastString = threeHoursPast.toString().padStart(2, '0')

                val nowIndex = times.indexOfFirst {
                    it.contains("T${nowHourString}:00")
                }

                val noonIndex = times.indexOfFirst {
                    it.contains("T${threeHoursPastString}:00")
                }

                val eveningIndex = times.indexOfFirst {
                    it.contains("T19:00")
                }

                weatherNow = nowIndex.takeIf { it != -1 }?.let {
                    temperatures[it] to weatherCodes[it]
                }

                weatherIn3Hours = noonIndex.takeIf { it != -1 }?.let {
                    temperatures[it] to weatherCodes[it]
                }

                weatherEvening = eveningIndex.takeIf { it != -1 }?.let {
                    temperatures[it] to weatherCodes[it]
                }
            } catch (e: Exception) {
                errorMessage = "Failed to load weather"
            } finally {
                isLoading = false
            }
        }
    }
}