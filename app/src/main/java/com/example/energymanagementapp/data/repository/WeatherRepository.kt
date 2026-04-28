package com.example.energymanagementapp.data.repository

import com.example.energymanagementapp.data.remote.weather.WeatherApi
import com.example.energymanagementapp.data.remote.weather.WeatherResponse

class WeatherRepository (
    private val weatherApi: WeatherApi
) {

    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double
    ): WeatherResponse {
        return weatherApi.getWeather(latitude, longitude)
    }
}