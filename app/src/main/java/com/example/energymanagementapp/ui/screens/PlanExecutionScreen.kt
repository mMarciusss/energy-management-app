package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.utils.getWeatherDescription

@Composable
fun PlanExecutionScreen(
    energy: Int,
    activities: List<PlanActivityWithBreak>,
    weatherTemperature: Double?,
    weatherCode: Int?,
    onConfirmComplete: (List<Int>) -> Unit
) {
    val checkedIds = remember { mutableStateListOf<Int>() }

    Column {
        Text("Remaining energy: $energy")

        LazyColumn {

            val now = System.currentTimeMillis()

            val pendingActivities = activities.filter { activity ->
                val breakRunning =
                    (activity.startTime ?: 0L) > 0L &&
                    activity.breakIsCompleted == false

                !activity.isCompleted && !breakRunning
            }

            items(pendingActivities) {activity ->

                val alreadySelectedWithBreak = activities.any {
                    checkedIds.contains(it.id) && it.breakDuration != null
                }

                val isThisWithBreak = activity.breakDuration != null

                val canSelect =
                    if (isThisWithBreak) {
                        !alreadySelectedWithBreak || checkedIds.contains(activity.id)
                    } else {
                        true
                    }

                Row{
                    Text(activity.activityName)

                    if(activity.breakDuration != null){
                        Text(" /Break: ${activity.breakDuration} min")
                    }

                    Checkbox(
                        checked = checkedIds.contains(activity.id),
                        enabled = canSelect,
                        onCheckedChange = {
                            if (checkedIds.contains(activity.id)) {
                                checkedIds.remove(activity.id)
                            } else {
                                checkedIds.add(activity.id)
                            }
                        }
                    )
                }
            }
        }

        if (checkedIds.isNotEmpty()) {
            Button(onClick = {
                onConfirmComplete(checkedIds.toList())
                checkedIds.clear()
            }) {
                Text("Confirm completed activities")
            }
        }

        Text("Completed:")

        LazyColumn {
            items(activities.filter { it.isCompleted }) {
                Text(it.activityName)
            }
        }

        Spacer(Modifier.height(16.dp))
        if(weatherTemperature != null && weatherCode != null) {
            Text("Temperature: $weatherTemperature")
            Text(getWeatherDescription(weatherCode))
        } else {
            Text("Loading weather...")
        }
    }
}