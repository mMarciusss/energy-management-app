package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.data.local.entities.ActivityEntity
import com.example.energymanagementapp.ui.accessibility.LocalAppColors
import com.example.energymanagementapp.ui.components.AppText
import com.example.energymanagementapp.ui.components.EnergyLeftIndicator
import com.example.energymanagementapp.ui.components.MainButton
import com.example.energymanagementapp.ui.components.WeatherLoadingRow
import com.example.energymanagementapp.ui.components.WeatherMiniRow
import com.example.energymanagementapp.ui.localization.LocalAppStrings
import java.util.Calendar

@Composable
fun ActivitySelectionScreen(
    activities: List<ActivityEntity>,
    selectedActivities: List<Int>,
    remainingEnergy: Int,
    totalEnergy: Int,
    weatherNow: Pair<Double, Int>?,
    weatherIn3Hours: Pair<Double, Int>?,
    weatherEvening: Pair<Double, Int>?,
    accessibilityMode: Boolean,
    onToggle: (ActivityEntity) -> Unit,
    onConfirm: () -> Unit,
    onToggleAccessibility: () -> Unit,
) {

    val colors = LocalAppColors.current

    val primaryGreen = colors.primary
    val background = colors.background
    val textGray = colors.textSecondary
    val titleColor = colors.textPrimary

    val strings = LocalAppStrings.current

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

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(
                start = 24.dp,
                end = 24.dp,
                bottom = 24.dp,
                top = 0.dp
            )
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    strings.chooseActivities,
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
                        contentDescription = strings.toggleAccessibilityMode,
                        tint = if (accessibilityMode)
                            colors.primary
                        else
                            colors.textSecondary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            AppText(
                strings.pickWhatYouWantToDoToday,
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
        ) {

            Spacer(Modifier.height(16.dp))
            EnergyLeftIndicator(
                remainingEnergy = remainingEnergy,
                totalEnergy = totalEnergy
            )
            Spacer(Modifier.height(16.dp))
            AppText(
                strings.weather,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                if (hasAnyWeather) {
                    WeatherMiniRow(
                        label = strings.now,
                        weather = weatherNow
                    )

                    if (showIn3h) {
                        WeatherMiniRow(
                            label = strings.inThreeHours,
                            weather = weatherIn3Hours
                        )
                    }

                    if (showEvening) {
                        WeatherMiniRow(
                            label = strings.evening,
                            weather = weatherEvening
                        )
                    }
                } else {
                    WeatherLoadingRow()
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
            text = strings.confirm,
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
                        AppText(strings.continueText)
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        AppText(strings.cancel)
                    }
                },
                title = { AppText(strings.unusedEnergy) },
                text = {
                    AppText(strings.youStillHaveEnergyLeft(remainingEnergy))
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
    val colors = LocalAppColors.current

    val primaryGreen = colors.primary

    val bgColor = when {
        selected -> colors.successBackground
        !enabled -> colors.disabledBackground
        else -> colors.card
    }

    val strings = LocalAppStrings.current

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(3.dp),
        border = if (selected) BorderStroke(1.dp, colors.border) else null
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                AppText(
                    text = activity.name,
                    fontWeight = FontWeight.Medium
                )

                AppText(
                    text = "${activity.energyCost} ${strings.spoons}",
                    color = colors.textSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Checkbox(
                checked = selected,
                enabled = enabled,
                onCheckedChange = { onClick() },
                colors = CheckboxDefaults.colors(
                    checkedColor = colors.primary,
                    uncheckedColor = colors.textSecondary,
                    disabledCheckedColor = colors.disabledText,
                    disabledUncheckedColor = colors.disabledText
                )
            )
        }
    }
}