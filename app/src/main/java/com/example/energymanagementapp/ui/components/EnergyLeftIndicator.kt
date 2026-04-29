package com.example.energymanagementapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.R

@Composable
fun EnergyLeftIndicator(energy: Int) {

    Column {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Energy left",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))
            Text("$energy", color = Color.Gray)
        }

        Spacer(Modifier.height(8.dp))

        val columns = 5
        val rows = (energy + columns - 1) / columns

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(rows) { rowIndex ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    repeat(columns) { colIndex ->
                        val index = rowIndex * columns + colIndex
                        val filled = index < energy

                        Image(
                            painter = painterResource(id = R.drawable.spoon),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            alpha = if (filled) 1f else 0.2f
                        )
                    }
                }
            }
        }
    }
}