package com.example.energymanagementapp.data.remote.weather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

object WeatherRetrofitInstance {

    // sukuriamas Retrofit klientas su bazine URL ir JSON konvertuotuoju
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // API interface implementacija
    val api: WeatherApi = retrofit.create(WeatherApi::class.java)
}