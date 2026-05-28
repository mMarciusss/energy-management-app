package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Schedule
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
import com.example.energymanagementapp.ui.components.SecondaryButton
import com.example.energymanagementapp.ui.localization.AppLanguage
import com.example.energymanagementapp.ui.localization.LocalAppStrings

@Composable
fun ActivityBreakListScreen(
    planActivities: List<PlanActivityWithBreak>,
    accessibilityMode: Boolean,
    onActivityClick: (Int, String) -> Unit,
    onBackToPlanCreation: () -> Unit,
    onToggleAccessibility: () -> Unit,
) {

    val colors = LocalAppColors.current

    val primaryGreen = colors.primary
    val background = colors.background
    val textGray = colors.textSecondary
    val titleColor = colors.textPrimary

    val strings = LocalAppStrings.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = strings.setBreaks,
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
                text = strings.assignBreaksToActivities,
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

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(planActivities) { activity ->

                val hasBreak = activity.breakDuration != null

                ActivityBreakItem(
                    activity = activity,
                    hasBreak = hasBreak,
                    onClick = {
                        onActivityClick(activity.id, activity.activityName)
                    }
                )
            }
        }

        SecondaryButton(
            text = strings.backToPlan,
            onClick = onBackToPlanCreation
        )
    }
}


@Composable
fun ActivityBreakItem(
    activity: PlanActivityWithBreak,
    hasBreak: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current

    val primaryGreen = colors.primary

    val bgColor = if (hasBreak) {
        colors.successBackground
    } else {
        colors.card
    }

    val strings = LocalAppStrings.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(3.dp),
        border = if (hasBreak) BorderStroke(1.dp, colors.border) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                AppText(
                    text = activity.activityName,
                    fontWeight = FontWeight.Medium
                )

                if (hasBreak) {
                    AppText(
                        text = "${strings.breakLabel}: ${activity.breakDuration} min",
                        color = primaryGreen,
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    AppText(
                        text = strings.noBreakSet,
                        color = colors.textSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Icon(
                imageVector = if (hasBreak)
                    Icons.Outlined.CheckCircle
                else
                    Icons.Outlined.Schedule,
                contentDescription = null,
                tint = if (hasBreak) primaryGreen else colors.textSecondary
            )
        }
    }
}