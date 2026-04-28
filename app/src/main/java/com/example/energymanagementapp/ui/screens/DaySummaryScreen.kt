package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.energymanagementapp.data.model.PlanActivityWithBreak

@Composable
fun DaySummaryScreen (
    activities: List<PlanActivityWithBreak>,
    totalEnergy: Int,
    totalEnergyUsed: Int,
    totalRestTimeMinutes: Int,
    onGoHome: () -> Unit
) {
    val completedActivities = activities.filter{it.isCompleted}
    val notCompletedActivities = activities.filter{!it.isCompleted}

    Column() {
        Text("Dienos apzvalga")

        Text("Sunaudota energija: $totalEnergyUsed / $totalEnergy")
        Text("Bendras poilsio laikas: $totalRestTimeMinutes min")

        Text("Atliktos veiklos:")

        LazyColumn() {
            items(completedActivities) { activity ->
                Text("${activity.activityName} completed at ${activity.completionTime}")
            }
        }

        Text("Neatliktos veiklos:")

        LazyColumn() {
            items(notCompletedActivities) { activity ->
                Text(activity.activityName)
            }
        }

        Button(onClick = onGoHome){
            Text("Back to home")
        }
    }
}