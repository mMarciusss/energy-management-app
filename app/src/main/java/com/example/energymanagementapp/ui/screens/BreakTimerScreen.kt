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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import kotlinx.coroutines.delay

@Composable
fun BreakTimerScreen(
    endTime: Long,
    onFinish: () -> Unit
) {
    val primaryGreen = Color(0xFF6BCB9A)
    val background = Color(0xFFF7F7F7)
    val textGray = Color(0xFF6B6B6B)

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
            horizontalArrangement = Arrangement.Start
        ) {
            Button(
                onClick = {
                    isRunning = false
                    onFinish()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C63FF).copy(alpha = 0.2f),
                    contentColor = Color(0xFF6C63FF)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Skip")
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Break time",
                color = textGray,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = String.format("%02d:%02d", minutes, seconds),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Relax and recover",
                color = textGray
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}