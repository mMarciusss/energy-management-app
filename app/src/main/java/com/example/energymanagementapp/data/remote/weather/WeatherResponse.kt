package com.example.energymanagementapp.data.remote.weather

import com.google.gson.annotations.SerializedName

// pagrindinis atsakymas iš API
data class WeatherResponse (
    val hourly: HourlyWeather
)

// valandiniai orų duomenys
data class HourlyWeather(
    // laikų sąrašas
    val time: List<String>,

    // temperatūra (mapinama iš temperature_2m)
    @SerializedName("temperature_2m")
    val temperature: List<Double>,

    // orų kodas (mapinama iš weather_code)
    @SerializedName("weather_code")
    val weatherCode: List<Int>
)