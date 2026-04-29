package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.ui.components.CircleButton

@Composable
fun BreakSetupScreen(
    activityName: String,
    breakDuration: Int,
    hasBreak: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onRemove: () -> Unit
) {

    val primaryGreen = Color(0xFF6BCB9A)
    val background = Color(0xFFF7F7F7)
    val textGray = Color(0xFF6B6B6B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                text = activityName,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Set break duration",
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    CircleButton("-", onDecrease)

                    Spacer(Modifier.width(20.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$breakDuration min",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Break",
                            color = textGray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(Modifier.width(20.dp))

                    CircleButton("+", onIncrease)
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Optional — helps with recovery",
                color = textGray,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Column {

            MainButton(
                text = if (hasBreak) "Update break" else "Confirm break",
                color = primaryGreen,
                onClick = onConfirm
            )

            Spacer(Modifier.height(12.dp))

            if (hasBreak) {
                SecondaryButton(
                    text = "Remove break",
                    onClick = onRemove
                )
            } else {
                SecondaryButton(
                    text = "Skip",
                    onClick = onCancel
                )
            }
        }
    }
}