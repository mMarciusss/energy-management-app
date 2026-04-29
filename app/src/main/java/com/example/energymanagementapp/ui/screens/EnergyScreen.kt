package com.example.energymanagementapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

import com.example.energymanagementapp.R
import com.example.energymanagementapp.ui.components.CircleButton

@Composable
fun EnergyScreen(
    energy: Int,
    minEnergy: Int,
    endTime: String,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val primaryGreen = Color(0xFF6BCB9A)
    val accentPurple = Color(0xFF6C63FF)
    val background = Color(0xFFF7F7F7)
    val textGray = Color(0xFF6B6B6B)

    var selectedTime by remember { mutableStateOf(endTime) }
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val locale = if (android.os.Build.VERSION.SDK_INT >= 24) {
        configuration.locales[0]
    } else {
        @Suppress("DEPRECATION")
        configuration.locale
    }

    val currentTime = SimpleDateFormat("HH:mm", locale).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                text = "Set energy",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "How much energy do you have today?",
                color = textGray
            )

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(primaryGreen, RoundedCornerShape(50))
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        "Energy",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                        repeat(4) { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                                repeat(5) { col ->
                                    val index = row * 5 + col
                                    val filled = index < energy

                                    Image(
                                        painter = painterResource(id = R.drawable.spoon),
                                        contentDescription = null,
                                        modifier = Modifier.size(36.dp),
                                        alpha = if (filled) 1f else 0.2f
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Text("$energy / 20", color = textGray)

                    Spacer(Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        CircleButton("-", onDecrease, energy > minEnergy)
                        CircleButton("+", onIncrease)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    "Ends at $selectedTime",
                    color = textGray
                )

                Spacer(Modifier.width(12.dp))

                SecondaryButton(
                    text = "Set time",
                    onClick = {
                        val calendar = Calendar.getInstance()

                        android.app.TimePickerDialog(
                            context,
                            { _, h, m ->
                                selectedTime =
                                    String.format(Locale.getDefault(), "%02d:%02d", h, m)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    }
                )
            }
        }

        Column {
            MainButton(
                text = "Confirm",
                color = primaryGreen,
                onClick = {
                    if (selectedTime <= currentTime) {
                        Toast.makeText(context, "Choose future time", Toast.LENGTH_SHORT).show()
                        return@MainButton
                    }
                    onConfirm(selectedTime)
                }
            )
        }
    }
}