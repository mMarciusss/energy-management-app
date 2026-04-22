package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.energymanagementapp.data.model.PlanActivityWithBreak

@Composable
fun PlanExecutionScreen(
    energy: Int,
    activities: List<PlanActivityWithBreak>,
    onToggleComplete: (Int) -> Unit
) {
    Column {
        Text("Remaining energy: $energy")

        LazyColumn {
            items(activities) {activity ->

                Row{
                    Text(activity.activityName)

                    if(activity.breakDuration != null){
                        Text(" /Break: ${activity.breakDuration} min")
                    }

                    Checkbox(
                        checked = activity.isCompleted,
                        onCheckedChange = {
                            onToggleComplete(activity.id)
                        }
                    )
                }
            }
        }
    }
}