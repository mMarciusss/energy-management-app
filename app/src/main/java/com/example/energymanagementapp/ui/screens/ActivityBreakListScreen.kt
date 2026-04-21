package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.energymanagementapp.data.model.PlanActivityWithDetails

@Composable
fun ActivityBreakListScreen(
    planActivities: List<PlanActivityWithDetails>,
    onActivityClick: (Int, String) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn {
            items(planActivities) { planActivity ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onActivityClick(planActivity.id, planActivity.activityName)
                        },
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(planActivity.activityName)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityBreakListScreenPreview() {

    val fakePlanActivities = listOf(
        PlanActivityWithDetails(
            id = 1,
            planDate = "2026-01-01",
            activityId = 1,
            isCompleted = false,
            completionTime = null,
            activityName = "Gym",
            energyCost = 2
        ),
        PlanActivityWithDetails(
            id = 2,
            planDate = "2026-01-01",
            activityId = 2,
            isCompleted = false,
            completionTime = null,
            activityName = "Study",
            energyCost = 1
        )
    )

    ActivityBreakListScreen(
        planActivities = fakePlanActivities,
        onActivityClick = { _, _ -> }
    )
}