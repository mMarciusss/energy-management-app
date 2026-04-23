package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.energymanagementapp.data.model.PlanActivityWithBreak

@Composable
fun PlanExecutionScreen(
    energy: Int,
    activities: List<PlanActivityWithBreak>,
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
                    (activity.endTime ?: 0L) > now

                !activity.isCompleted && !breakRunning
            }

            items(pendingActivities) {activity ->

                Row{
                    Text(activity.activityName)

                    if(activity.breakDuration != null){
                        Text(" /Break: ${activity.breakDuration} min")
                    }

                    Checkbox(
                        checked = checkedIds.contains(activity.id),
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
    }
}