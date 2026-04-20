package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.energymanagementapp.data.local.entities.ActivityEntity
import com.example.energymanagementapp.data.local.entities.PlanActivityEntity

@Composable
fun ActivityBreakListScreen(
    planActivities: List<PlanActivityEntity>,
    activities: List<ActivityEntity>,
    onActivityClick: (Int, String) -> Unit
) {
    val activityMap = activities.associateBy { it.id }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        planActivities.forEach { planActivity ->

            val activityName =
                activityMap[planActivity.activityId]?.name ?: "Unknown activity"

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onActivityClick(planActivity.id, activityName)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(activityName)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityBreakListScreenPreview() {

    val fakeActivities = listOf(
        ActivityEntity(id = 1, name = "Gym", energyCost = 2),
        ActivityEntity(id = 2, name = "Study", energyCost = 1)
    )

    val fakePlanActivities = listOf(
        PlanActivityEntity(id = 1, activityId = 1, planDate = "2026-01-01"),
        PlanActivityEntity(id = 2, activityId = 2, planDate = "2026-01-01")
    )

    ActivityBreakListScreen(
        planActivities = fakePlanActivities,
        activities = fakeActivities,
        onActivityClick = {_, _ ->}
    )
}