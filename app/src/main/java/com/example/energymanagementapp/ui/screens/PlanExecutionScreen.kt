package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.utils.getWeatherDescription
import com.example.energymanagementapp.utils.getWeatherIcon
import java.util.Calendar

@Composable
fun PlanExecutionScreen(
    energy: Int,
    activities: List<PlanActivityWithBreak>,
    weatherNow: Pair<Double, Int>?,
    weatherIn3Hours: Pair<Double, Int>?,
    weatherEvening: Pair<Double, Int>?,
    onConfirmComplete: (List<Int>) -> Unit,
    onGoHome: () -> Unit
) {
    val nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val in3hHour = (nowHour + 3) % 24

    val showIn3h = nowHour <= 20
    val showEvening = when {
        in3hHour >= 19 -> false
        nowHour < 19 -> true
        else -> false
    }

    val checkedIds = remember { mutableStateListOf<Int>() }

    Column {
        Text("Remaining energy: $energy")

        LazyColumn {

            val now = System.currentTimeMillis()

            val pendingActivities = activities.filter { activity ->
                val breakRunning =
                    (activity.startTime ?: 0L) > 0L &&
                    activity.breakIsCompleted == false

                !activity.isCompleted && !breakRunning
            }

            items(pendingActivities) {activity ->

                val alreadySelectedWithBreak = activities.any {
                    checkedIds.contains(it.id) && it.breakDuration != null
                }

                val isThisWithBreak = activity.breakDuration != null

                val canSelect =
                    if (isThisWithBreak) {
                        !alreadySelectedWithBreak || checkedIds.contains(activity.id)
                    } else {
                        true
                    }

                Row{
                    Text(activity.activityName)

                    if(activity.breakDuration != null){
                        Text(" /Break: ${activity.breakDuration} min")
                    }

                    Checkbox(
                        checked = checkedIds.contains(activity.id),
                        enabled = canSelect,
                        onCheckedChange = {
                            if (checkedIds.contains(activity.id)) {
                                checkedIds.remove(activity.id)
                            } else {
                                checkedIds.add(activity.id)
                            }
                        }
                    )
                }
            }
        }

        if (checkedIds.isNotEmpty()) {
            Button(onClick = {
                onConfirmComplete(checkedIds.toList())
                checkedIds.clear()
            }) {
                Text("Confirm completed activities")
            }
        }

        Text("Completed:")

        LazyColumn {
            items(activities.filter { it.isCompleted }) {
                Text(it.activityName)
            }
        }

        Spacer(Modifier.height(16.dp))
        if(weatherNow != null && weatherIn3Hours != null && weatherEvening != null) {
            Text("Today's weather:")
            Row(verticalAlignment = Alignment.CenterVertically){
                Text("Current weather: ${weatherNow.first} °C, ${getWeatherDescription(weatherNow.second)} ")
                Icon(
                    imageVector = getWeatherIcon(weatherNow.second),
                    contentDescription = null
                )
            }
            if(showIn3h){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Weather in 3 hours from now: ${weatherIn3Hours.first} °C, ${getWeatherDescription(weatherIn3Hours.second)} ")
                    Icon(
                        imageVector = getWeatherIcon(weatherIn3Hours.second),
                        contentDescription = null
                    )
                }
            }
            if(showEvening){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Evening weather: ${weatherEvening.first} °C, ${getWeatherDescription(weatherEvening.second)} ")
                    Icon(
                        imageVector = getWeatherIcon(weatherEvening.second),
                        contentDescription = null
                    )
                }
            }
            Text("Take weather into consideration when choosing activities!")
        } else {
            Text("Loading today's weather...")
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = onGoHome) {
            Text("Go home")
        }
    }
}