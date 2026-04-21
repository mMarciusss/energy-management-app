package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.data.model.PlanActivityWithDetails

@Composable
fun PlanCreationHomeScreen(
    energy: Int,
    isEnergySet: Boolean,
    onGoToEnergyScreen: () -> Unit,
    onGoToActivitySelection: () -> Unit,
    onGoToBreakScreen: () -> Unit,
    selectedActivities: List<PlanActivityWithDetails>
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
            Spacer(Modifier.height(16.dp))
        }

        Button(onClick = onGoToEnergyScreen){
            Text("Nustatyti dienos energiją")
        }

        if(isEnergySet) {
            Spacer(Modifier.height(16.dp))
            Button(onClick = onGoToActivitySelection) {
                Text("Pasirinkti veiklas")
            }
        }

        if(selectedActivities.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.height(150.dp)
            ) {
                items(selectedActivities) {
                    Text(it.activityName)
                }
            }

            Spacer(Modifier.height(16.dp))
            Button(onClick = onGoToBreakScreen) {
                Text("Nustatyti pertraukas")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlanCreationHomeScreenPreview(){
    PlanCreationHomeScreen(
        energy = 5,
        isEnergySet = true,
        onGoToEnergyScreen = {},
        onGoToActivitySelection = {},
        onGoToBreakScreen = {},
        selectedActivities = listOf(
            PlanActivityWithDetails(1,"2026-04-21",1,false, null,"Gym", 3),
            PlanActivityWithDetails(2,"2026-04-21",2,false, null,"Read", 2)
        )
    )
}