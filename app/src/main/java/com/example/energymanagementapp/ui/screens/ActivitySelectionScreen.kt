package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.energymanagementapp.data.local.entities.ActivityEntity

@Composable
fun ActivitySelectionScreen(
    activities: List<ActivityEntity>,
    selectedActivities: List<Int>,
    onToggle: (Int) -> Unit,
    onConfirm: () -> Unit
) {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        activities.forEach { activity ->
            val checked = selectedActivities.contains(activity.id)

            Row(verticalAlignment = Alignment.CenterVertically){
                Text(activity.name)

                Checkbox(
                    checked = checked,
                    onCheckedChange = { onToggle(activity.id) }
                )
            }
        }

        Button(onClick = onConfirm) {
            Text("Confirm")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivitySelectionPreview(){
    ActivitySelectionScreen(
        activities = listOf(
            ActivityEntity(1, "Workout", 3),
            ActivityEntity(2, "Reading", 1),
            ActivityEntity(3, "Coding", 2),
            ActivityEntity(4, "Gaming", 2),
            ActivityEntity(5, "Walking", 1)
        ),
        selectedActivities = listOf(1),
        onToggle = {},
        onConfirm = {}
    )
}