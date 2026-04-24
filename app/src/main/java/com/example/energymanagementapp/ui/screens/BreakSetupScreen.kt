package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BreakSetupScreen(
    activityName: String,
    breakDuration: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = activityName)

        Row(verticalAlignment = Alignment.CenterVertically) {

            Button(onClick = onDecrease) {
                Text("-")
            }

            Spacer(Modifier.width(16.dp))

            Text("Break duration: $breakDuration minutes")

            Spacer(Modifier.width(16.dp))

            Button(onClick = onIncrease) {
                Text("+")
            }
        }

        Spacer(Modifier.width(16.dp))

        Row(horizontalArrangement = Arrangement.Center){
            Button(onClick = onCancel) {
                Text("Cancel break")
            }
            Spacer(Modifier.width(16.dp))

            Button(onClick = onConfirm) {
                Text("Confirm break")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BreakSetupScreenPreview() {
    BreakSetupScreen(
        activityName = "Gym",
        breakDuration = 30,
        onIncrease = {},
        onDecrease = {},
        onConfirm = {},
        onCancel = {}
    )
}