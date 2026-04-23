package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay

@Composable
fun BreakTimerScreen (
    endTime: Long,
    onFinish: () -> Unit
) {
    var currentTime by remember { mutableStateOf(System.currentTimeMillis())}

    LaunchedEffect(Unit) {
        while (true){
            delay(1000)
            currentTime = System.currentTimeMillis()
        }
    }

    val timeLeft = ((endTime - currentTime) / 1000).toInt()

    if(timeLeft <= 0) {
        onFinish()
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = String.format("%02d:%02d", minutes, seconds)
        )

        Button(onClick = onFinish) {
            Text("Skip")
        }
    }
}