package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.data.model.PlanActivityWithBreak

@Composable
fun DaySummaryScreen(
    activities: List<PlanActivityWithBreak>,
    totalEnergy: Int,
    totalEnergyUsed: Int,
    totalRestTimeMinutes: Int,
    isFromCalendar: Boolean,
    onGoHome: () -> Unit,
    onGoBack: (() -> Unit)? = null
) {
    val primaryGreen = Color(0xFF6BCB9A)
    val secondaryBlue = Color(0xFF6982B5)
    val background = Color(0xFFF7F7F7)
    val textGray = Color(0xFF6B6B6B)

    val completedActivities = activities.filter { it.isCompleted }
    val notCompletedActivities = activities.filter { !it.isCompleted }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            Text(
                text = "Day summary",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "How your day went",
                color = textGray
            )

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(primaryGreen, RoundedCornerShape(50))
            )

            Spacer(Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {

                    Text(
                        "Energy used",
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        "$totalEnergyUsed / $totalEnergy",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Total rest time",
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        "$totalRestTimeMinutes min",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            if (completedActivities.isNotEmpty()) {

                Text(
                    "Completed",
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    completedActivities.forEach {
                        ActivitySummaryItem(
                            name = it.activityName,
                            time = it.completionTime,
                            completed = true
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
            }

            if (notCompletedActivities.isNotEmpty()) {

                Text(
                    "Not completed",
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    notCompletedActivities.forEach {
                        ActivitySummaryItem(
                            name = it.activityName,
                            time = null,
                            completed = false
                        )
                    }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

            if (isFromCalendar && onGoBack != null) {
                SecondaryButton(
                    text = "Go back",
                    onClick = onGoBack
                )
            }

            MainButton(
                text = "Go home",
                color = primaryGreen,
                onClick = onGoHome
            )
        }
    }
}


@Composable
fun ActivitySummaryItem(
    name: String,
    time: String?,
    completed: Boolean
) {
    val primaryGreen = Color(0xFF6BCB9A)

    val bgColor = if (completed) Color(0xFFE8F5EE) else Color.White
    val textGray = Color(0xFF6B6B6B)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(2.dp),
        border = if (completed) BorderStroke(1.dp, primaryGreen) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(name, fontWeight = FontWeight.Medium)

                if (time != null) {
                    Text(
                        "Completed at $time",
                        color = textGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (completed) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = primaryGreen
                )
            }
        }
    }
}