package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.ui.accessibility.LocalAppColors
import com.example.energymanagementapp.ui.components.AppText
import com.example.energymanagementapp.ui.localization.LocalAppStrings
import kotlinx.coroutines.delay

@Composable
fun BreakTimerScreen(
    endTime: Long,
    accessibilityMode: Boolean,
    onFinish: () -> Unit,
    onToggleAccessibility: () -> Unit
) {
    val colors = LocalAppColors.current

    val primaryGreen = colors.primary
    val background = colors.background
    val textGray = colors.textSecondary
    val accent = colors.accent
    val titleColor = colors.textPrimary

    val strings = LocalAppStrings.current

    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var isRunning by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (isRunning) {
            delay(1000)
            currentTime = System.currentTimeMillis()
        }
    }

    val timeLeft = ((endTime - currentTime) / 1000).toInt()

    if (timeLeft <= 0 && isRunning) {
        isRunning = false
        onFinish()
    }

    val minutes = (timeLeft.coerceAtLeast(0)) / 60
    val seconds = (timeLeft.coerceAtLeast(0)) % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    isRunning = false
                    onFinish()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = accent.copy(alpha = 0.18f),
                    contentColor = accent
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                AppText(strings.skip)
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AppText(
                text = strings.breakTime,
                color = textGray,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(16.dp))

            AppText(
                text = String.format("%02d:%02d", minutes, seconds),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = titleColor
            )

            Spacer(Modifier.height(12.dp))

            AppText(
                text = strings.relaxAndRecover,
                color = textGray
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}