package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

    Column {
        HorizontalCalendar(
            state = state,
            dayContent = { calendarDay ->

                val status = dayStatuses.find { it.date == calendarDay.date }?.status

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

        Button(onClick = onGoHome) {
            Text("Go Home")
        }
    }
}