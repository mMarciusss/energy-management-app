package com.example.energymanagementapp.data.remote.weather

import com.google.gson.annotations.SerializedName

data class WeatherResponse (
    val current: CurrentWeather
)

data class CurrentWeather(
    @SerializedName("temperature_2m")
    val temperature: Double,

    @SerializedName("weather_code")
    val weatherCode: Int
)