package com.example.energymanagementapp.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.energymanagementapp.core.state.PlanState
import com.example.energymanagementapp.ui.accessibility.LocalAccessibilitySettings
import com.example.energymanagementapp.ui.accessibility.LocalAppColors
import com.example.energymanagementapp.ui.components.AppText
import com.example.energymanagementapp.ui.components.MainButton
import com.example.energymanagementapp.ui.components.SecondaryButton

@Composable
fun HomeScreen(
    planState: PlanState,
    isTooLateToStart: Boolean,
    accessibilityMode: Boolean,
    onStartPlan: () -> Unit,
    onContinuePlan: () -> Unit,
    onViewPlan: () -> Unit,
    onViewSummary: () -> Unit,
    onViewPastDays: () -> Unit,
    onManageActivities: () -> Unit,
    onToggleAccessibility: () -> Unit
) {

    val colors = LocalAppColors.current

    val primaryGreen = colors.primary
    val background = colors.background
    val accent = colors.accent
    val textGray = colors.textSecondary
    val titleColor = colors.textPrimary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp),
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
                    text = "Welcome",
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

            AppText(
                text = "Let's manage your day",
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
                text = "Energy Manager",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(32.dp))

            when (planState) {

                PlanState.NOT_STARTED -> {
                    MainButton(
                        text = "Start Plan",
                        color = primaryGreen,
                        enabled = !isTooLateToStart,
                        onClick = onStartPlan
                    )

                    if (isTooLateToStart) {
                        Spacer(Modifier.height(8.dp))

                        AppText(
                            text = "Too late to start today",
                            color = textGray
                        )

                        AppText(
                            text = "Come back tomorrow morning",
                            color = textGray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                PlanState.CREATING -> {
                    MainButton(
                        text = "Continue Plan",
                        color = accent,
                        onClick = onContinuePlan
                    )

                    Spacer(Modifier.height(8.dp))
                    AppText(
                        "Cancel plan to manage activities",
                        color = textGray
                    )
                }

                PlanState.CONFIRMED -> {
                    MainButton(
                        text = "View Today Plan",
                        color = primaryGreen,
                        onClick = onViewPlan
                    )

                    Spacer(Modifier.height(8.dp))
                    AppText(
                        "Finish plan to manage activities",
                        color = textGray
                    )
                }

                PlanState.COMPLETED -> {
                    MainButton(
                        text = "View Summary",
                        color = accent,
                        onClick = onViewSummary
                    )

                    Spacer(Modifier.height(8.dp))
                    AppText(
                        "Day completed ✔",
                        color = primaryGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            SecondaryButton(
                text = "Past days",
                onClick = onViewPastDays
            )

            Spacer(Modifier.height(12.dp))

            SecondaryButton(
                text = "Manage activities",
                enabled = planState == PlanState.NOT_STARTED ||
                        planState == PlanState.COMPLETED,
                onClick = onManageActivities
            )
        }

        Spacer(modifier = Modifier.height(1.dp))
    }
}