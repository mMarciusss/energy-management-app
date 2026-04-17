package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.energymanagementapp.viewmodel.EnergyViewModel

@Composable
fun EnergyScreen(
    viewModel: EnergyViewModel
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Energy: ${viewModel.energy}")

        Button(onClick = {viewModel.increaseEnergy()}) {
            Text(text = "Add energy")
        }
    }
}