package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.data.local.entities.ActivityEntity
import com.example.energymanagementapp.utils.getWeatherDescription
import com.example.energymanagementapp.utils.getWeatherIcon
import com.example.energymanagementapp.R
import com.example.energymanagementapp.ui.components.EnergyLeftIndicator
import com.example.energymanagementapp.ui.components.WeatherMiniRow
import java.util.Calendar

@Composable
fun ActivitySelectionScreen(
    activities: List<ActivityEntity>,
    selectedActivities: List<Int>,
    remainingEnergy: Int,
    weatherNow: Pair<Double, Int>?,
    weatherIn3Hours: Pair<Double, Int>?,
    weatherEvening: Pair<Double, Int>?,
    onToggle: (ActivityEntity) -> Unit,
    onConfirm: () -> Unit
) {

    val primaryGreen = Color(0xFF6BCB9A)
    val secondaryBlue = Color(0xFF6982B5)
    val background = Color(0xFFF7F7F7)
    val textGray = Color(0xFF6B6B6B)

    val nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val in3hHour = (nowHour + 3) % 24

    val showIn3h = nowHour <= 20
    val showEvening = when {
        in3hHour >= 19 -> false
        nowHour < 19 -> true
        else -> false
    }

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                "Choose activities",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "Pick what you want to do today",
                color = textGray
            )

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(primaryGreen, RoundedCornerShape(50))
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Spacer(Modifier.height(16.dp))
            EnergyLeftIndicator(remainingEnergy)

            Spacer(Modifier.height(16.dp))
            Text(
                "Weather",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                WeatherMiniRow(
                    label = "Now",
                    weather = weatherNow
                )

                if (showIn3h) {
                    WeatherMiniRow(
                        label = "In 3 hours",
                        weather = weatherIn3Hours
                    )
                }

                if (showEvening) {
                    WeatherMiniRow(
                        label = "Evening",
                        weather = weatherEvening
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(activities) { activity ->

                    val selected = selectedActivities.contains(activity.id)
                    val canSelect = remainingEnergy >= activity.energyCost

                    ActivityItem(
                        activity = activity,
                        selected = selected,
                        enabled = selected || canSelect,
                        onClick = { onToggle(activity) }
                    )
                }
            }
        }

        MainButton(
            text = "Confirm",
            color = primaryGreen,
            onClick = {
                if (remainingEnergy > 0) {
                    showDialog = true
                } else {
                    onConfirm()
                }
            }
        )

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false
                        onConfirm()
                    }) {
                        Text("Continue")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Unused energy") },
                text = {
                    Text("You still have $remainingEnergy energy left.")
                }
            )
        }
    }
}


@Composable
fun ActivityItem(
    activity: ActivityEntity,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val primaryGreen = Color(0xFF6BCB9A)
    val disabledColor = Color(0xFFEAEAEA)

    val bgColor = when {
        selected -> Color(0xFFE8F5EE)
        !enabled -> Color(0xFFF2F2F2)
        else -> Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(3.dp),
        border = if (selected) BorderStroke(1.dp, primaryGreen) else null
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.name,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "${activity.energyCost} energy",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Checkbox(
                checked = selected,
                enabled = enabled,
                onCheckedChange = { onClick() },
                colors = CheckboxDefaults.colors(
                    checkedColor = primaryGreen,
                    uncheckedColor = Color.Gray
                )
            )
        }
    }
}