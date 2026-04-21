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
import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.data.model.PlanActivityWithDetails

@Composable
fun ActivityBreakListScreen(
    planActivities: List<PlanActivityWithBreak>,
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

                    if(planActivity.breakDuration != null){
                        Text(" /Break: ${planActivity.breakDuration} min")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityBreakListScreenPreview() {

    val fakePlanActivities = listOf(
        PlanActivityWithBreak(
            id = 1,
            activityName = "Gym",
            energyCost = 2,
            breakDuration = 45
        ),
        PlanActivityWithBreak(
            id = 2,
            activityName = "Study",
            energyCost = 1,
            breakDuration = null
        )
    )

    ActivityBreakListScreen(
        planActivities = fakePlanActivities,
        onActivityClick = { _, _ -> }
    )
}