package com.example.energymanagementapp.ui.screens

import android.widget.Space
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
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.viewmodel.EnergyViewModel

@Composable
fun EnergyScreen(
    viewModel: EnergyViewModel
){
    Row (
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){

        Button(onClick = {viewModel.decreaseEnergy()}) {
            Text("-")
        }

        Spacer(Modifier.width(16.dp))
        Text(text = "Energy: ${viewModel.energy}")

        Spacer(Modifier.width(16.dp))
        Button(onClick = {viewModel.increaseEnergy()}) {
            Text(text = "+")
        }
    }
}