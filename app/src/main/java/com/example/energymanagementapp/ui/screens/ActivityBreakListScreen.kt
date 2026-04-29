package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.data.model.PlanActivityWithBreak

@Composable
fun ActivityBreakListScreen(
    planActivities: List<PlanActivityWithBreak>,
    onActivityClick: (Int, String) -> Unit,
    onBackToPlanCreation: () -> Unit
) {

    val primaryGreen = Color(0xFF6BCB9A)
    val secondaryBlue = Color(0xFF6982B5)
    val background = Color(0xFFF7F7F7)
    val textGray = Color(0xFF6B6B6B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                text = "Set breaks",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Assign breaks to your activities",
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

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(planActivities) { activity ->

                val hasBreak = activity.breakDuration != null

                ActivityBreakItem(
                    activity = activity,
                    hasBreak = hasBreak,
                    onClick = {
                        onActivityClick(activity.id, activity.activityName)
                    }
                )
            }
        }

        SecondaryButton(
            text = "Back to plan",
            onClick = onBackToPlanCreation
        )
    }
}


@Composable
fun ActivityBreakItem(
    activity: PlanActivityWithBreak,
    hasBreak: Boolean,
    onClick: () -> Unit
) {
    val primaryGreen = Color(0xFF6BCB9A)

    val bgColor = if (hasBreak) {
        Color(0xFFF2F2F2)
    } else {
        Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(3.dp),
        border = if (hasBreak) BorderStroke(1.dp, primaryGreen) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = activity.activityName,
                    fontWeight = FontWeight.Medium
                )

                if (hasBreak) {
                    Text(
                        text = "Break: ${activity.breakDuration} min",
                        color = primaryGreen,
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    Text(
                        text = "No break set",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Icon(
                imageVector = if (hasBreak)
                    Icons.Outlined.CheckCircle
                else
                    Icons.Outlined.Schedule,
                contentDescription = null,
                tint = if (hasBreak) primaryGreen else Color.Gray
            )
        }
    }
}