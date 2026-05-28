package com.example.energymanagementapp.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.data.local.entities.ActivityEntity
import com.example.energymanagementapp.R
import com.example.energymanagementapp.ui.accessibility.LocalAppColors
import com.example.energymanagementapp.ui.components.AppText
import com.example.energymanagementapp.ui.components.MainButton
import com.example.energymanagementapp.ui.components.SecondaryButton
import com.example.energymanagementapp.ui.localization.LocalAppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageActivitiesScreen(
    activities: List<ActivityEntity>,
    accessibilityMode: Boolean,
    onBackToHome: () -> Unit,
    onAdd: (String, Int) -> Unit,
    onDelete: (ActivityEntity) -> Unit,
    onToggleAccessibility: () -> Unit
) {
    val colors = LocalAppColors.current

    val primaryGreen = colors.primary
    val background = colors.background
    val textGray = colors.textSecondary
    val titleColor = colors.textPrimary

    val strings = LocalAppStrings.current

    var name by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedEnergy by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .navigationBarsPadding()
            .padding(
                start = 24.dp,
                end = 24.dp,
                bottom = 24.dp,
                top = 0.dp
            )
            .statusBarsPadding()
    ) {

        Column(modifier = Modifier.weight(1f)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    strings.manageActivitiesTitle,
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

            AppText(strings.createNewActivitiesOrDeleteExistingOnes, color = textGray)

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(primaryGreen, RoundedCornerShape(50))
            )

            Spacer(Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = colors.card),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    AppText(strings.newActivity, fontWeight = FontWeight.Medium)

                    Spacer(Modifier.height(12.dp))

                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { AppText(strings.activityName) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(10.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            value = selectedEnergy?.let { "$it ${strings.spoons}" } ?: "",
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { AppText(strings.energyCost) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
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
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                                repeat(value) {
                                                    Image(
                                                        painter = painterResource(id = R.drawable.spoon),
                                                        contentDescription = null,
                                                        modifier = Modifier.size(28.dp)
                                                    )
                                                }
                                            }

                                            Spacer(Modifier.weight(1f))

                                            AppText("$value")
                                        }
                                    },
                                    onClick = {
                                        selectedEnergy = value
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    MainButton(
                        text = strings.addActivity,
                        color = primaryGreen,
                        onClick = {
                            val energy = selectedEnergy ?: return@MainButton
                            if (name.isBlank()) return@MainButton

                            onAdd(name, energy)

                            name = ""
                            selectedEnergy = null
                        }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            AppText(strings.yourActivities, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(activities) { activity ->
                    ActivityManageItem(
                        activity = activity,
                        onDelete = { onDelete(activity) }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        SecondaryButton(
            text = strings.backToHome,
            onClick = onBackToHome
        )
    }
}

@Composable
fun ActivityManageItem(
    activity: ActivityEntity,
    onDelete: () -> Unit
) {
    val colors = LocalAppColors.current
    val textGray = colors.textSecondary

    val strings = LocalAppStrings.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.card),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                AppText(
                    text = activity.name,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )

                Spacer(Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        repeat(activity.energyCost) {
                            Image(
                                painter = painterResource(id = R.drawable.spoon),
                                contentDescription = null,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(Modifier.width(6.dp))

                    AppText(
                        text = "${activity.energyCost}",
                        color = textGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Button(
                onClick = onDelete,
                modifier = Modifier.width(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accent
                )
            ) {
                AppText(strings.delete)
            }
        }
    }
}