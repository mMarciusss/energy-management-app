package com.example.energymanagementapp.data.remote.weather

import com.google.gson.annotations.SerializedName

data class WeatherResponse (
    val hourly: HourlyWeather
)

data class HourlyWeather(
    val time: List<String>,

    @SerializedName("temperature_2m")
    val temperature: List<Double>,

    @SerializedName("weather_code")
    val weatherCode: List<Int>
)