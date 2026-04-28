package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState


@Composable
fun PastDaysScreen(
    datesWithPlans: List<LocalDate>,
    onDateClick: (LocalDate) -> Unit
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

    HorizontalCalendar(
        state = state,
        dayContent = { calendarDay ->

            val hasPlan = datesWithPlans.contains(calendarDay.date)

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp)
                    .background(
                        if (hasPlan) Color.Green else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable {
                        if (hasPlan) {
                            onDateClick(calendarDay.date)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(calendarDay.date.dayOfMonth.toString())
            }
        }
    )
}