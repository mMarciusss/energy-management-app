package com.example.energymanagementapp.ui.screens

import android.widget.Button
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.migration.Migration
import com.example.energymanagementapp.core.state.PlanState
import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.data.model.PlanActivityWithDetails
import com.example.energymanagementapp.utils.getWeatherDescription
import com.example.energymanagementapp.utils.getWeatherIcon

@Composable
fun PlanCreationHomeScreen(
    energy: Int,
    isEnergySet: Boolean,
    endTime: String,
    planState: PlanState,
    weatherTemperature: Double?,
    weatherCode: Int?,
    onGoHome: () -> Unit,
    onCancelPlan: () -> Unit,
    onGoToEnergyScreen: () -> Unit,
    onGoToActivitySelection: () -> Unit,
    onGoToBreakScreen: () -> Unit,
    onConfirmPlan: () -> Unit,
    selectedActivities: List<PlanActivityWithBreak>
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
            Text(
                text = "Plano Pabaiga: $endTime",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(16.dp))
        }

        Button(onClick = onGoToEnergyScreen){
            Text("Nustatyti dienos energiją ir plano pabaigos laiką")
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
                items(selectedActivities) { planActivity ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(planActivity.activityName)

                        if(planActivity.breakDuration != null){
                            Text(" /Break: ${planActivity.breakDuration} min")
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Button(onClick = onGoToBreakScreen) {
                Text("Nustatyti pertraukas")
            }

            if(isEnergySet && selectedActivities.isNotEmpty()){
                Spacer(Modifier.height(30.dp))
                Button(onClick = onConfirmPlan) {
                    Text("Confirm plan")
                }
            }

        }
        Spacer(Modifier.height(16.dp))
        if(weatherTemperature != null && weatherCode != null) {
            Text("Today's weather:")
            Text("Temperature: $weatherTemperature °C")
            Text(getWeatherDescription(weatherCode))

            Icon(
                imageVector = getWeatherIcon(weatherCode),
                contentDescription = null
            )

        } else {
            Text("Loading today's weather...")
        }

        Spacer(Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Button(onClick = onGoHome) {
                Text("Go home")
            }
            if(planState != PlanState.NOT_STARTED){
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = onCancelPlan) {
                    Text("Cancel plan")
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PlanCreationHomeScreenPreview(){
//    PlanCreationHomeScreen(
//        energy = 5,
//        isEnergySet = true,
//        onGoToEnergyScreen = {},
//        onGoToActivitySelection = {},
//        onGoToBreakScreen = {},
//        onConfirmPlan = {},
//        selectedActivities = listOf(
//            PlanActivityWithBreak(
//                id = 1,
//                planDate = "2026-04-22",
//                activityId = 1,
//                isCompleted = false,
//                completionTime = null,
//                activityName = "Gym",
//                energyCost = 2,
//                breakDuration = 45),
//            PlanActivityWithBreak(
//                id = 2,
//                planDate = "2026-04-22",
//                activityId = 2,
//                isCompleted = false,
//                completionTime = null,
//                activityName = "Study",
//                energyCost = 1,
//                breakDuration = null
//            )
//        )
//    )
//}