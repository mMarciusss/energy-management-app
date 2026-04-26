package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.energymanagementapp.data.local.entities.ActivityEntity

@Composable
fun ManageActivitiesScreen(
    activities: List<ActivityEntity>,
    onAdd: (String, Int) -> Unit,
    onDelete: (Int) -> Unit
) {
    Column() {
        LazyColumn() {
            items(activities) { activity ->
                Row() {
                    Text(activity.name)
                    Text("${activity.energyCost}")

                    Button(onClick = {onDelete(activity.id)}) {
                        Text("Delete")
                    }
                }
            }
        }
    }

    var name by remember { mutableStateOf("") }
    var energyText by remember { mutableStateOf("") }

    TextField(
        value = name,
        onValueChange = { name = it },
        label = { Text("Activity name") }
    )

    TextField(
        value = energyText,
        onValueChange = { energyText = it},
        label = { Text("Energy cost") }
    )


    Button(onClick = {
        val energy = energyText.toIntOrNull() ?: return@Button
        if (name.isBlank()) return@Button

        onAdd(name, energy)

        name = ""
        energyText = ""
    }) {
        Text("Add")
    }
}