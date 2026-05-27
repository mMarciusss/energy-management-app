package com.example.energymanagementapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

fun getWeatherIcon(code: Int): ImageVector {
    return when (code) {
        0 -> Icons.Default.WbSunny
        1 -> Icons.Default.WbSunny
        2 -> Icons.Default.CloudQueue
        3 -> Icons.Default.Cloud

        45, 48 -> Icons.Default.BlurOn
        in 51..67 -> Icons.Default.Umbrella
        in 71..77 -> Icons.Default.AcUnit
        in 80..82 -> Icons.Default.Thunderstorm
        in 95..99 -> Icons.Default.FlashOn

        else -> Icons.AutoMirrored.Filled.Help
    }
}