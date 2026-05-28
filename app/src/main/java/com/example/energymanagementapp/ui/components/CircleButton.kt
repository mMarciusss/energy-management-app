package com.example.energymanagementapp.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.ui.accessibility.LocalAccessibilitySettings
import com.example.energymanagementapp.ui.accessibility.LocalAppColors

@Composable
fun CircleButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    val accessibility = LocalAccessibilitySettings.current
    val colors = LocalAppColors.current

    val textColor = if (enabled) {
        Color.White
    } else {
        colors.disabledText
    }

    val bgColor = if (enabled) {
        colors.accent
    } else {
        colors.disabledBackground
    }

    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = CircleShape,
        color = bgColor,
        shadowElevation = if (enabled) 4.dp else 0.dp,
        tonalElevation = 0.dp,
        modifier = Modifier.size(accessibility.buttonHeight.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            AppText(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}