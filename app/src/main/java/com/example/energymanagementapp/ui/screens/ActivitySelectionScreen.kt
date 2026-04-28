package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.data.local.entities.ActivityEntity
import com.example.energymanagementapp.utils.getWeatherDescription
import java.util.Calendar

@Composable
fun ActivitySelectionScreen(
    activities: List<ActivityEntity>,
    selectedActivities: List<Int>,
    remainingEnergy: Int,
    weatherNow: Pair<Double, Int>?,
    weatherIn3Hours: Pair<Double, Int>?,
    weatherEvening: Pair<Double, Int>?,
    onToggle: (ActivityEntity) -> Unit,
    onConfirm: () -> Unit
) {
    val nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val in3hHour = (nowHour + 3) % 24

    val showIn3h = nowHour <= 20
    val showEvening = when {
        in3hHour >= 19 -> false
        nowHour < 19 -> true
        else -> false
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Energijos likutis: $remainingEnergy")
        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(activities) { activity ->
                val checked = selectedActivities.contains(activity.id)

                Row(verticalAlignment = Alignment.CenterVertically){
                    Text(activity.name)

                    val canSelect = remainingEnergy >= activity.energyCost

                    Checkbox(
                        checked = checked,
                        enabled = checked || canSelect,
                        onCheckedChange = { onToggle(activity) }
                    )
                }
            }
        }

        Button(onClick = onConfirm) {
            Text("Confirm")
        }

        Spacer(Modifier.height(16.dp))
        if(weatherNow != null && weatherIn3Hours != null && weatherEvening != null) {
            Text("Today's weather:")
            Text("Current weather: ${weatherNow.first} °C, ${getWeatherDescription(weatherNow.second)}")
            if(showIn3h){
                Text("Weather in 3 hours from now: ${weatherIn3Hours.first} °C, ${getWeatherDescription(weatherIn3Hours.second)}")
            }
            if(showEvening){
                Text("Evening weather: ${weatherEvening.first} °C, ${getWeatherDescription(weatherEvening.second)}")
            }
            Text("Take weather into consideration when choosing activities!")
        } else {
            Text("Loading today's weather...")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ActivitySelectionPreview(){
//    ActivitySelectionScreen(
//        activities = listOf(
//            ActivityEntity(1, "Workout", 3),
//            ActivityEntity(2, "Reading", 1),
//            ActivityEntity(3, "Coding", 2),
//            ActivityEntity(4, "Gaming", 2),
//            ActivityEntity(5, "Walking", 1)
//        ),
//        selectedActivities = listOf(1),
//        remainingEnergy = 5,
//        onToggle = {},
//        onConfirm = {}
//    )
//}