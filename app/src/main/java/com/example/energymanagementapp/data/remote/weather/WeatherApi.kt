package com.example.energymanagementapp.data.remote.weather

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") current: String = "temperature_2m,weather_code"
    ): WeatherResponse
}