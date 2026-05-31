package com.example.energymanagementapp.ui.screens

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.core.state.PlanState
import com.example.energymanagementapp.ui.accessibility.LocalAppColors
import com.example.energymanagementapp.ui.components.AppText
import com.example.energymanagementapp.ui.components.MainButton
import com.example.energymanagementapp.ui.components.SecondaryButton
import com.example.energymanagementapp.ui.localization.AppLanguage
import com.example.energymanagementapp.ui.localization.LocalAppStrings

@Composable
fun HomeScreen(
    planState: PlanState,
    isTooLateToStart: Boolean,
    accessibilityMode: Boolean,
    selectedLanguage: AppLanguage,
    onStartPlan: () -> Unit,
    onContinuePlan: () -> Unit,
    onViewPlan: () -> Unit,
    onViewSummary: () -> Unit,
    onViewPastDays: () -> Unit,
    onManageActivities: () -> Unit,
    onToggleAccessibility: () -> Unit,
    onToggleLanguage: () -> Unit
) {

    val colors = LocalAppColors.current

    val primaryGreen = colors.primary
    val background = colors.background
    val accent = colors.accent
    val textGray = colors.textSecondary
    val titleColor = colors.textPrimary

    val strings = LocalAppStrings.current

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

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                AppText(
                    text = strings.welcome,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = titleColor
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                color = colors.background,
                                shape = CircleShape
                            )
                            .clickable {
                                onToggleLanguage()
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        AppText(
                            text = if (selectedLanguage == AppLanguage.EN)
                                "🇬🇧"
                            else
                                "🇱🇹",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

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
            }

            Spacer(Modifier.height(6.dp))

            AppText(
                text = strings.letsManageYourDay,
                style = MaterialTheme.typography.bodyMedium,
                color = textGray
            )

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(
                        color = primaryGreen,
                        shape = RoundedCornerShape(50)
                    )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AppText(
                text = strings.energyManager,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(32.dp))

            when (planState) {

                PlanState.NOT_STARTED -> {
                    MainButton(
                        text = strings.startPlan,
                        color = primaryGreen,
                        enabled = !isTooLateToStart,
                        onClick = onStartPlan
                    )

                    if (isTooLateToStart) {
                        Spacer(Modifier.height(8.dp))

                        AppText(
                            text = strings.tooLateToStartToday,
                            color = textGray
                        )

                        AppText(
                            text = strings.comeBackTomorrowMorning,
                            color = textGray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                PlanState.CREATING -> {
                    MainButton(
                        text = strings.continuePlan,
                        color = accent,
                        onClick = onContinuePlan
                    )

                    Spacer(Modifier.height(8.dp))
                    AppText(
                        strings.cancelPlanToManageActivities,
                        color = textGray
                    )
                }

                PlanState.CONFIRMED -> {
                    MainButton(
                        text = strings.viewTodayPlan,
                        color = primaryGreen,
                        onClick = onViewPlan
                    )

                    Spacer(Modifier.height(8.dp))
                    AppText(
                        strings.finishPlanToManageActivities,
                        color = textGray
                    )
                }

                PlanState.COMPLETED -> {
                    MainButton(
                        text = strings.viewSummary,
                        color = accent,
                        onClick = onViewSummary
                    )

                    Spacer(Modifier.height(8.dp))
                    AppText(
                        strings.dayCompleted,
                        color = primaryGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            SecondaryButton(
                text = strings.pastDays,
                onClick = onViewPastDays
            )

            Spacer(Modifier.height(12.dp))

            SecondaryButton(
                text = strings.manageActivities,
                enabled = planState == PlanState.NOT_STARTED ||
                        planState == PlanState.COMPLETED,
                onClick = onManageActivities
            )
        }

        Spacer(modifier = Modifier.height(1.dp))
    }
}