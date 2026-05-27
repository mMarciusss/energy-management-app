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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.ui.accessibility.LocalAppColors
import com.example.energymanagementapp.ui.components.AppText
import com.example.energymanagementapp.ui.components.EnergyLeftIndicator
import com.example.energymanagementapp.ui.components.MainButton
import com.example.energymanagementapp.ui.components.SecondaryButton
import com.example.energymanagementapp.ui.components.WeatherLoadingRow
import com.example.energymanagementapp.ui.components.WeatherMiniRow
import com.example.energymanagementapp.utils.getWeatherDescription
import com.example.energymanagementapp.utils.getWeatherIcon
import java.util.Calendar

@Composable
fun PlanExecutionScreen(
    energy: Int,
    totalEnergy: Int,
    activities: List<PlanActivityWithBreak>,
    weatherNow: Pair<Double, Int>?,
    weatherIn3Hours: Pair<Double, Int>?,
    weatherEvening: Pair<Double, Int>?,
    accessibilityMode: Boolean,
    onConfirmComplete: (List<Int>) -> Unit,
    onGoHome: () -> Unit,
    onCancelPlan: () -> Unit,
    onToggleAccessibility: () -> Unit
) {

    val colors = LocalAppColors.current

    val primaryGreen = colors.primary
    val background = colors.background
    val textGray = colors.textSecondary
    val titleColor = colors.textPrimary

    val nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val in3hHour = (nowHour + 3) % 24

    val showIn3h = nowHour <= 20
    val showEvening = when {
        in3hHour >= 19 -> false
        nowHour < 19 -> true
        else -> false
    }

    val hasAnyWeather = weatherNow != null ||
            weatherIn3Hours != null ||
            weatherEvening != null

    val checkedIds = remember { mutableStateListOf<Int>() }

    val pending = activities.filter { !it.isCompleted }
    val completed = activities.filter { it.isCompleted }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText(
                text = "Your plan",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = titleColor
            )

            IconButton(
                onClick = onToggleAccessibility
            ) {
                Icon(
                    imageVector = Icons.Outlined.Accessibility,
                    contentDescription = "Toggle accessibility mode",
                    tint = if (accessibilityMode)
                        colors.primary
                    else
                        colors.textSecondary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        AppText("Best of luck in completing your tasks!", color = textGray)

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(primaryGreen, RoundedCornerShape(50))
        )

        Spacer(Modifier.height(16.dp))
        EnergyLeftIndicator(
            remainingEnergy = energy,
            totalEnergy = totalEnergy
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                SectionTitle("To do")
            }

            items(pending) { activity ->

                val selected = checkedIds.contains(activity.id)

                ExecutionItem(
                    activity = activity,
                    selected = selected,
                    onToggle = {
                        if (selected) checkedIds.remove(activity.id)
                        else checkedIds.add(activity.id)
                    }
                )
            }

            if (checkedIds.isNotEmpty()) {
                item {
                    MainButton(
                        text = "Confirm completed",
                        color = primaryGreen,
                        onClick = {
                            onConfirmComplete(checkedIds.toList())
                            checkedIds.clear()
                        }
                    )
                }
            }

            if (completed.isNotEmpty()) {
                item {
                    SectionTitle("Completed")
                }

                items(completed) { activity ->
                    CompletedItem(activity)
                }
            }

            item {
                Spacer(Modifier.height(16.dp))
                AppText(
                    "Weather",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    if (hasAnyWeather) {
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
                    } else {
                        WeatherLoadingRow()
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }

        if (completed.isEmpty()) {

            Spacer(Modifier.height(12.dp))

            SecondaryButton(
                text = "Cancel plan",
                onClick = onCancelPlan
            )

            Spacer(Modifier.height(6.dp))

            AppText(
                text = "Once you complete any activity, you won't be able to cancel the plan",
                color = textGray,
                style = MaterialTheme.typography.bodySmall
            )
        }

        SecondaryButton(
            text = "Go home",
            onClick = onGoHome
        )
    }
}


@Composable
fun ExecutionItem(
    activity: PlanActivityWithBreak,
    selected: Boolean,
    onToggle: () -> Unit
) {
    val colors = LocalAppColors.current

    val primaryGreen = colors.primary
    val textGray = colors.textSecondary

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected)
                colors.successBackground
            else
                colors.card
        ),
        elevation = CardDefaults.cardElevation(3.dp),
        border = if (selected)
            BorderStroke(1.dp, colors.border)
        else null
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                AppText(activity.activityName, fontWeight = FontWeight.Medium)

                if (activity.breakDuration != null) {
                    AppText(
                        "Break: ${activity.breakDuration} min",
                        color = textGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Checkbox(
                checked = selected,
                onCheckedChange = { onToggle() }
            )
        }
    }
}


@Composable
fun CompletedItem(activity: PlanActivityWithBreak) {

    val colors = LocalAppColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.disabledBackground
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        AppText(
            text = activity.activityName,
            modifier = Modifier.padding(16.dp),
            color = colors.disabledText
        )
    }
}


@Composable
fun SectionTitle(text: String) {
    AppText(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
        )
    )
}