package com.example.energymanagementapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.utils.getWeatherDescription
import com.example.energymanagementapp.utils.getWeatherIcon

@Composable
fun WeatherMiniRow(
    label: String,
    weather: Pair<Double, Int>?
) {

    if (weather == null) return

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = label,
                modifier = Modifier.width(60.dp),
                color = Color(0xFF6B6B6B),
                style = MaterialTheme.typography.bodySmall
            )

            Icon(
                imageVector = getWeatherIcon(weather.second),
                contentDescription = null,
                tint = Color(0xFF6982B5)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                "${weather.first}°C · ${getWeatherDescription(weather.second)}",
                color = Color.Gray
            )
        }
    }
}