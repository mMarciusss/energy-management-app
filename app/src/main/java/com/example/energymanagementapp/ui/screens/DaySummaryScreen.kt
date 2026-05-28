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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.ui.accessibility.LocalAppColors
import com.example.energymanagementapp.ui.components.AppText
import com.example.energymanagementapp.ui.components.MainButton
import com.example.energymanagementapp.ui.components.SecondaryButton
import com.example.energymanagementapp.ui.localization.LocalAppStrings

@Composable
fun DaySummaryScreen(
    activities: List<PlanActivityWithBreak>,
    totalEnergy: Int,
    totalEnergyUsed: Int,
    totalRestTimeMinutes: Int,
    isFromCalendar: Boolean,
    accessibilityMode: Boolean,
    onGoHome: () -> Unit,
    onGoBack: (() -> Unit)? = null,
    onToggleAccessibility: () -> Unit
) {
    val colors = LocalAppColors.current

    val primaryGreen = colors.primary
    val background = colors.background
    val textGray = colors.textSecondary
    val titleColor = colors.textPrimary

    val strings = LocalAppStrings.current

    val completedActivities = activities
        .filter { it.isCompleted }
        .sortedBy { it.completionTime ?: "99:99" }
    val notCompletedActivities = activities.filter { !it.isCompleted }

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
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        text = strings.daySummary,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = titleColor
                    )

                    IconButton(onClick = onToggleAccessibility) {
                        Icon(
                            imageVector = Icons.Outlined.Accessibility,
                            contentDescription = strings.toggleAccessibilityMode,
                            tint = if (accessibilityMode) colors.primary else colors.textSecondary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                AppText(
                    text = strings.howYourDayWent,
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
                    colors = CardDefaults.cardColors(containerColor = colors.card),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        AppText(strings.energyUsed, fontWeight = FontWeight.Medium)

                        Spacer(Modifier.height(4.dp))

                        AppText(
                            "$totalEnergyUsed / $totalEnergy",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(12.dp))

                        AppText(strings.totalRestTime, fontWeight = FontWeight.Medium)

                        Spacer(Modifier.height(4.dp))

                        AppText(
                            "$totalRestTimeMinutes min",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
            }

            if (completedActivities.isNotEmpty()) {
                item {
                    AppText(strings.completed, fontWeight = FontWeight.Bold)
                }

                items(completedActivities) {
                    ActivitySummaryItem(
                        name = it.activityName,
                        timeText = it.completionTime?.let { time -> strings.completedAt(time) },
                        completed = true
                    )
                }
            }

            if (notCompletedActivities.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(8.dp))
                    AppText(strings.notCompleted, fontWeight = FontWeight.Bold)
                }

                items(notCompletedActivities) {
                    ActivitySummaryItem(
                        name = it.activityName,
                        timeText = null,
                        completed = false
                    )
                }
            }

            item {
                Spacer(Modifier.height(16.dp))
            }
        }

        Column(
            modifier = Modifier.navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            if (isFromCalendar && onGoBack != null) {
                SecondaryButton(
                    text = strings.goBack,
                    onClick = onGoBack
                )
            }

            MainButton(
                text = strings.goHome,
                color = primaryGreen,
                onClick = onGoHome
            )
        }
    }
}


@Composable
fun ActivitySummaryItem(
    name: String,
    timeText: String?,
    completed: Boolean
) {
    val colors = LocalAppColors.current

    val primaryGreen = colors.primary
    val bgColor = if (completed) colors.successBackground else colors.card
    val textGray = colors.textSecondary

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(2.dp),
        border = if (completed) BorderStroke(1.dp, colors.border) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                AppText(name, fontWeight = FontWeight.Medium)

                if (timeText != null) {
                    AppText(
                        timeText,
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