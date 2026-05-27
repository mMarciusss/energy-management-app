package com.example.energymanagementapp.utils

import com.example.energymanagementapp.ui.localization.AppStrings

fun getWeatherDescription(code: Int, strings: AppStrings): String {
    return when (code) {
        0 -> strings.clearSky
        1 -> strings.mainlyClear
        2 -> strings.partlyCloudy
        3 -> strings.cloudy
        45, 48 -> strings.fog
        51, 53, 55 -> strings.drizzle
        61, 63, 65 -> strings.rain
        71, 73, 75 -> strings.snow
        80, 81, 82 -> strings.rainShowers
        95 -> strings.thunderstorm
        else -> strings.unknownWeather
    }
}