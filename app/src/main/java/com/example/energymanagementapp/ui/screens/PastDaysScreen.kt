package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.energymanagementapp.viewmodel.DayStatus
import com.example.energymanagementapp.viewmodel.Status
import java.time.LocalDate
import java.time.YearMonth
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState


@Composable
fun PastDaysScreen(
    dayStatuses: List<DayStatus>,
    onDateClick: (LocalDate) -> Unit,
    onGoHome: () -> Unit
) {
    val currentMonth = YearMonth.of(
        LocalDate.now().year,
        LocalDate.now().month
    )

    val state = rememberCalendarState(
        startMonth = currentMonth.minusMonths(12),
        endMonth = currentMonth.plusMonths(12),
        firstVisibleMonth = currentMonth
    )

    val today = LocalDate.now()

    Column {

        HorizontalCalendar(
            state = state,
            dayContent = { calendarDay ->

                val status = dayStatuses.find { it.date == calendarDay.date }?.status
                val isToday = calendarDay.date == today

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(40.dp)
                        .background(
                            when (status) {
                                Status.COMPLETED -> Color.Green
                                Status.PARTIAL -> Color.Yellow
                                Status.NOT_COMPLETED -> Color.Red
                                else -> Color.Transparent
                            },
                            shape = CircleShape

                        )
                        .then(
                            if(isToday) {
                                Modifier.border(
                                    width = 2.dp,
                                    color = Color.Black,
                                    shape = CircleShape
                                )
                            } else Modifier
                        )
                        .clickable {
                            if (status != null) {
                                onDateClick(calendarDay.date)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(calendarDay.date.dayOfMonth.toString())
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(Color.Green, "Completed")
            LegendItem(Color.Yellow, "Partial")
            LegendItem(Color.Red, "Not completed")
        }


        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onGoHome) {
            Text("Go Home")
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color, shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text)
    }
}