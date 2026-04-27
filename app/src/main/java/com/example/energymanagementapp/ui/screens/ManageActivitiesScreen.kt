package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.data.local.entities.ActivityEntity

@Composable
fun ManageActivitiesScreen(
    activities: List<ActivityEntity>,
    onBackToHome: () -> Unit,
    onAdd: (String, Int) -> Unit,
    onDelete: (ActivityEntity) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var energyText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Manage Activities",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Activity name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        TextField(
            value = energyText,
            onValueChange = { energyText = it },
            label = { Text("Energy cost") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                val energy = energyText.toIntOrNull() ?: return@Button
                if (name.isBlank()) return@Button

                onAdd(name, energy)

                name = ""
                energyText = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add activity")
        }

        Spacer(Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(activities) { activity ->
                ActivityItem(
                    activity = activity,
                    onDelete = { onDelete(activity) }
                )
            }
        }

        Button(onClick = onBackToHome){
            Text("Back to home")
        }
    }
}

@Composable
fun ActivityItem(
    activity: ActivityEntity,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            Text(activity.name, style = MaterialTheme.typography.bodyLarge)
            Text("Energy: ${activity.energyCost}", style = MaterialTheme.typography.bodySmall)
        }

        Button(onClick = onDelete) {
            Text("Delete")
        }
    }
}