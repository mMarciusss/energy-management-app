package com.example.energymanagementapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.energymanagementapp.ui.accessibility.LocalAppColors
import com.example.energymanagementapp.ui.components.AppText
import com.example.energymanagementapp.ui.components.CircleButton
import com.example.energymanagementapp.ui.components.MainButton
import com.example.energymanagementapp.ui.components.SecondaryButton
import com.example.energymanagementapp.ui.localization.AppLanguage
import com.example.energymanagementapp.ui.localization.LocalAppLanguage
import com.example.energymanagementapp.ui.localization.LocalAppStrings

@SuppressLint("LocalContextConfigurationRead")
@Composable
fun EnergyScreen(
    energy: Int,
    minEnergy: Int,
    endTime: String,
    accessibilityMode: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onConfirm: (String) -> Unit,
    onToggleAccessibility: () -> Unit
) {
    val colors = LocalAppColors.current

    val primaryGreen = colors.primary
    val accentPurple = colors.accent
    val background = colors.background
    val textGray = colors.textSecondary
    val titleColor = colors.textPrimary

    val strings = LocalAppStrings.current
    val appLanguage = LocalAppLanguage.current
    val context = LocalContext.current

    var selectedTime by remember { mutableStateOf(endTime) }

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
            .padding(
                start = 24.dp,
                end = 24.dp,
                bottom = 24.dp,
                top = 0.dp
            )
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = strings.setEnergy,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = titleColor
                )

                IconButton(
                    onClick = onToggleAccessibility
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Accessibility,
                        contentDescription = strings.toggleAccessibilityMode,
                        tint = if (accessibilityMode)
                            colors.primary
                        else
                            colors.textSecondary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            AppText(
                text = strings.howMuchEnergyToday,
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
                colors = CardDefaults.cardColors(containerColor = colors.card),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AppText(
                        text = strings.energy.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = titleColor
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

                    AppText("$energy / 20", color = textGray)

                    Spacer(Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        CircleButton("-", onDecrease, energy > minEnergy)
                        CircleButton("+", onIncrease, energy < 20)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                AppText(
                    strings.endsAt(selectedTime),
                    color = textGray
                )

                Spacer(Modifier.width(12.dp))

                SecondaryButton(
                    text = strings.setTime,
                    onClick = {
                        val calendar = Calendar.getInstance()

                        val locale = when (appLanguage) {
                            AppLanguage.LT -> Locale("lt")
                            AppLanguage.EN -> Locale.ENGLISH
                        }

                        Locale.setDefault(locale)

                        val config = Configuration(context.resources.configuration)
                        config.setLocale(locale)

                        context.resources.updateConfiguration(
                            config,
                            context.resources.displayMetrics
                        )

                        val dialog = android.app.TimePickerDialog(
                            android.view.ContextThemeWrapper(
                                context,
                                android.R.style.Theme_Material_Light_Dialog_Alert
                            ),
                            { _, h, m ->
                                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", h, m)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        )

                        dialog.show()
                        dialog.window?.attributes = dialog.window?.attributes?.apply {
                            y = -80
                        }
                    }
                )
            }
        }

        Column (
            modifier = Modifier.navigationBarsPadding()
        ) {
            MainButton(
                text = strings.confirm,
                color = primaryGreen,
                onClick = {
                    if (selectedTime <= currentTime) {
                        Toast.makeText(context, strings.chooseFutureTime, Toast.LENGTH_SHORT).show()
                        return@MainButton
                    }
                    onConfirm(selectedTime)
                }
            )
        }
    }
}