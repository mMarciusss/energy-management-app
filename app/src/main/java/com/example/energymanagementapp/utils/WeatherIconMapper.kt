package com.example.energymanagementapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

fun getWeatherIcon(code: Int): ImageVector {
    return when (code) {

        // clear
        0 -> Icons.Default.WbSunny

        // partly cloudy
        1, 2 -> Icons.Default.Cloud

        // cloudy
        3 -> Icons.Default.CloudQueue

        // fog
        45, 48 -> Icons.Default.BlurOn

        // rain
        in 51..67 -> Icons.Default.Umbrella

        // snow
        in 71..77 -> Icons.Default.AcUnit

        // heavy rain
        in 80..82 -> Icons.Default.Thunderstorm

        // storm
        in 95..99 -> Icons.Default.FlashOn

        else -> Icons.AutoMirrored.Filled.Help
    }
}