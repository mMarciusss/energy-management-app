package com.example.energymanagementapp.ui.screens

import android.R.attr.enabled
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.core.state.PlanState

@Composable
fun HomeScreen(
    planState: PlanState,
    onStartPlan: () -> Unit,
    onContinuePlan: () -> Unit,
    onViewPlan: () -> Unit,
    onViewSummary: () -> Unit,
    onViewPastDays: () -> Unit,
    onManageActivities: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (planState) {
            PlanState.NOT_STARTED -> {
                Button(onClick = onStartPlan) {
                    Text("Start plan")
                }
            }

            PlanState.CREATING -> {
                Button(onClick = onContinuePlan) {
                    Text("Continue plan creation")
                }
            }

            PlanState.CONFIRMED -> {
                Button(onClick = onViewPlan) {
                    Text("View your day plan")
                }
            }

            PlanState.COMPLETED -> {
                Button(onClick = onViewSummary) {
                    Text("View day summary")
                }

                Spacer(Modifier.height(16.dp))
                Text("Wait for tomorrow morning to start a new plan")
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = onViewPastDays) {
            Text("View past days")
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onManageActivities,
            enabled = planState == PlanState.NOT_STARTED ||
                    planState == PlanState.COMPLETED
        ) {
            Text("Manage activities")
        }

        if (planState == PlanState.CREATING) {
           Text("Cancel your day plan to manage activities")
        }

        if (planState == PlanState.CONFIRMED) {
            Text("Complete your day plan to manage activities")
        }
    }
}