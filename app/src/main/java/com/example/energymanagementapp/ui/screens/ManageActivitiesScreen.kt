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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageActivitiesScreen(
    activities: List<ActivityEntity>,
    onBackToHome: () -> Unit,
    onAdd: (String, Int) -> Unit,
    onDelete: (ActivityEntity) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedEnergy by remember { mutableStateOf<Int?>(null) }

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

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedEnergy?.toString() ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Energy cost") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                (1..5).forEach { value ->
                    DropdownMenuItem(
                        text = { Text(value.toString()) },
                        onClick = {
                            selectedEnergy = value
                            expanded = false
                        }
                    )
                }
            }
        }



        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                val energy = selectedEnergy ?: return@Button
                if (energy !in 1..5) return@Button
                if (name.isBlank()) return@Button

                onAdd(name, energy)

                name = ""
                selectedEnergy = null
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