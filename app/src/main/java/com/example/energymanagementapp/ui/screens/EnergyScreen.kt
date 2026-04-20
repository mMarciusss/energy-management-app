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
fun EnergyScreen(
    energy: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onConfirm: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Button(onClick = onDecrease) {
                Text("-")
            }

            Spacer(Modifier.width(16.dp))
            Text(text = "Energy: $energy")

            Spacer(Modifier.width(16.dp))
            Button(onClick = onIncrease) {
                Text(text = "+")
            }
        }
        Spacer(Modifier.width(16.dp))

        Button(onClick = onConfirm) {
            Text("Confirm energy level")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EnergyScreenPreview() {
    EnergyScreen(
        energy = 5,
        onIncrease = {},
        onDecrease = {},
        onConfirm = {}
    )
}