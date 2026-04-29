package com.example.energymanagementapp.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.core.state.PlanState

@Composable
fun HomeScreen(
    planState: PlanState,
    isTooLateToStart: Boolean,
    onStartPlan: () -> Unit,
    onContinuePlan: () -> Unit,
    onViewPlan: () -> Unit,
    onViewSummary: () -> Unit,
    onViewPastDays: () -> Unit,
    onManageActivities: () -> Unit
) {

    val primaryGreen = Color(0xFF6BCB9A)
    val background = Color(0xFFF7F7F7)
    val accent = Color(0xFF6C63FF)

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
            Text(
                text = "Welcome",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Let's manage your day",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6B6B6B)
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

            Text(
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
                        Text(
                            text = "Wait for tomorrow morning",
                            color = Color(0xFF6B6B6B)
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
                    Text(
                        "Cancel plan to manage activities",
                        color = Color(0xFF6B6B6B)
                    )
                }

                PlanState.CONFIRMED -> {
                    MainButton(
                        text = "View Today Plan",
                        color = primaryGreen,
                        onClick = onViewPlan
                    )

                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Finish plan to manage activities",
                        color = Color(0xFF6B6B6B)
                    )
                }

                PlanState.COMPLETED -> {
                    MainButton(
                        text = "View Summary",
                        color = accent,
                        onClick = onViewSummary
                    )

                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Day completed",
                        color = primaryGreen
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


@Composable
fun MainButton(
    text: String,
    color: Color,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        label = ""
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(20.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            disabledContainerColor = color.copy(alpha = 0.3f)
        )
    ) {
        Text(text)
    }
}


@Composable
fun SecondaryButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        label = ""
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(20.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (enabled) 4.dp else 0.dp,
            pressedElevation = 1.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6982B5),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFF0F0F0),
            disabledContentColor = Color.Gray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Text(text)
    }
}