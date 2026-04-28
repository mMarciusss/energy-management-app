package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PastDaysScreen(
    dates: List<String>,
    onSelectDate: (String) -> Unit
) {
    LazyColumn() {
        items(dates) { date ->
            Button(onClick = { onSelectDate(date) }) {
                Text(date)
            }
        }
    }
}