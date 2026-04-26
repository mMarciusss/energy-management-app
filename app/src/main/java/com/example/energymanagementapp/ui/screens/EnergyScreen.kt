package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.Locale

@Composable
fun EnergyScreen(
    energy: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onConfirm: (String) -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Button(onClick = onDecrease) {
                Text("-")
            }

            Spacer(Modifier.width(16.dp))
            Text(text = "Energy: $energy")

            Spacer(Modifier.width(16.dp))
            Button(onClick = onIncrease) {
                Text(text = "+")
            }
        }
        Spacer(Modifier.width(16.dp))
        var selectedTime by remember { mutableStateOf("20:00") }
        val context = LocalContext.current
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Plan ends at: $selectedTime")
            Spacer(Modifier.width(16.dp))

            Button(onClick = {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                android.app.TimePickerDialog(
                    context,
                    { _, pickedHour, pickedMinute ->
                        selectedTime = String.format(Locale.getDefault(), "%02d:%02d", pickedHour, pickedMinute)
                    },
                    hour,
                    minute,
                    true
                ).show()
            }) {
                Text("Set plan time")
            }
        }

        Spacer(Modifier.width(16.dp))
        Button(onClick = {
            onConfirm(selectedTime)
        }) {
            Text("Confirm energy and plan deadline")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EnergyScreenPreview() {
    EnergyScreen(
        energy = 5,
        onIncrease = {},
        onDecrease = {},
        onConfirm = {}
    )
}