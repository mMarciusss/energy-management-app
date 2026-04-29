package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.core.state.PlanState
import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.utils.getWeatherDescription
import com.example.energymanagementapp.utils.getWeatherIcon
import com.example.energymanagementapp.R

@Composable
fun PlanCreationHomeScreen(
    energy: Int,
    isEnergySet: Boolean,
    endTime: String,
    planState: PlanState,
    weatherTemperature: Double?,
    weatherCode: Int?,
    onGoHome: () -> Unit,
    onCancelPlan: () -> Unit,
    onGoToEnergyScreen: () -> Unit,
    onGoToActivitySelection: () -> Unit,
    onGoToBreakScreen: () -> Unit,
    onConfirmPlan: () -> Unit,
    selectedActivities: List<PlanActivityWithBreak>
) {
    val primaryGreen = Color(0xFF6BCB9A)
    val secondaryBlue = Color(0xFF6982B5)
    val background = Color(0xFFF7F7F7)
    val textGray = Color(0xFF6B6B6B)

    val hasActivities = selectedActivities.isNotEmpty()
    val hasBreaks = selectedActivities.any { it.breakDuration != null }

    Image(
        painter = painterResource(id = R.drawable.spoon),
        contentDescription = "Energy spoon",
        modifier = Modifier.size(18.dp)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Column {
            Text(
                text = "Create plan",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Set your energy, choose activities and start your day",
                style = MaterialTheme.typography.bodyMedium,
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
            modifier = Modifier
                .weight(1f)
                .padding(top = 16.dp),
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                if (isEnergySet) {
                    item {
                        EnergySummaryCard(
                            energy = energy,
                            endTime = endTime
                        )
                    }
                }

                item {
                    StepButton(
                        text = if (isEnergySet) "Energy set" else "Set energy and end time",
                        completed = isEnergySet,
                        color = if (isEnergySet) secondaryBlue else primaryGreen,
                        onClick = onGoToEnergyScreen
                    )
                }

                if (isEnergySet) {

                    if (hasActivities) {
                        item {
                            SelectedActivitiesCard(
                                selectedActivities = selectedActivities
                            )
                        }

                        item {
                            StepButton(
                                text = "Activities selected",
                                completed = true,
                                color = secondaryBlue,
                                onClick = onGoToActivitySelection
                            )
                        }
                    } else {
                        item {
                            StepButton(
                                text = "Choose activities",
                                completed = false,
                                color = primaryGreen,
                                onClick = onGoToActivitySelection
                            )
                        }
                    }
                }

                if (hasActivities) {

                    item {
                        StepButton(
                            text = if (hasBreaks) "Breaks configured" else "Set breaks · Optional",
                            completed = hasBreaks,
                            color = secondaryBlue,
                            onClick = onGoToBreakScreen
                        )
                    }

                    item {
                        MainButton(
                            text = "Confirm plan",
                            color = primaryGreen,
                            onClick = onConfirmPlan
                        )
                    }
                }

                item {
                    WeatherMiniCard(
                        weatherTemperature = weatherTemperature,
                        weatherCode = weatherCode
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SecondaryButton(
                    text = "Go home",
                    onClick = onGoHome
                )
            }

            if (planState != PlanState.NOT_STARTED) {
                Box(modifier = Modifier.weight(1f)) {
                    SecondaryButton(
                        text = "Cancel plan",
                        onClick = onCancelPlan
                    )
                }
            }
        }
    }
}

@Composable
fun EnergySummaryCard(
    energy: Int,
    endTime: String
) {
    val primaryGreen = Color(0xFF6BCB9A)
    val textGray = Color(0xFF6B6B6B)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Energy",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = "$energy / 20",
                    color = textGray
                )
            }

            Spacer(Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {

                repeat(4) { rowIndex ->
                    Row(horizontalArrangement = Arrangement.spacedBy(0.dp)) {

                        repeat(5) { colIndex ->
                            val index = rowIndex * 5 + colIndex
                            val filled = index < energy

                            Image(
                                painter = painterResource(id = R.drawable.spoon),
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                alpha = if (filled) 1f else 0.2f
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = energy / 20f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = Color(0xFF6BCB9A),
                trackColor = Color(0xFFE0E0E0)
            )

            Spacer(Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = textGray,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "Plan ends at $endTime",
                    color = textGray
                )
            }
        }
    }
}

@Composable
fun StepButton(
    text: String,
    completed: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    if (completed) {
        SecondaryButton(
            text = "✓ $text",
            onClick = onClick
        )
    } else {
        MainButton(
            text = text,
            color = color,
            onClick = onClick
        )
    }
}

@Composable
fun SelectedActivitiesCard(
    selectedActivities: List<PlanActivityWithBreak>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Selected activities",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn {
                items(selectedActivities) { activity ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = activity.activityName,
                            color = Color(0xFF333333)
                        )

                        if (activity.breakDuration != null) {
                            Text(
                                text = " · Break ${activity.breakDuration} min",
                                color = Color(0xFF6B6B6B)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherMiniCard(
    weatherTemperature: Double?,
    weatherCode: Int?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (weatherTemperature != null && weatherCode != null) {
                Icon(
                    imageVector = getWeatherIcon(weatherCode),
                    contentDescription = null,
                    tint = Color(0xFF6982B5),
                    modifier = Modifier.size(28.dp)
                )

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Today's weather",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = "$weatherTemperature °C · ${getWeatherDescription(weatherCode)}",
                        color = Color(0xFF6B6B6B)
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Outlined.Cloud,
                    contentDescription = null,
                    tint = Color(0xFF6982B5),
                    modifier = Modifier.size(28.dp)
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = "Loading today's weather...",
                    color = Color(0xFF6B6B6B)
                )
            }
        }
    }
}