package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlanCreationHomeScreen(
    energy: Int,
    isEnergySet: Boolean,
    onGoToEnergyScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Plano sudarymo ekranas")

        Spacer(Modifier.height(16.dp))

        if(isEnergySet) {
            Text("Dabartinė energija: $energy")
        }
        Spacer(Modifier.height(16.dp))

        Button(onClick = onGoToEnergyScreen){
            Text("Nustatyti dienos energiją")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlanCreationHomeScreenPreview(){
    PlanCreationHomeScreen(
        energy = 5,
        isEnergySet = true,
        onGoToEnergyScreen = {}
    )
}