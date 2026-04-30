package com.example.energymanagementapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    val primaryGreen = Color(0xFF6BCB9A)
    val background = Color(0xFFF7F7F7)
    val textGray = Color(0xFF6B6B6B)

    val currentMonth = YearMonth.now()

    val state = rememberCalendarState(
        startMonth = currentMonth.minusMonths(12),
        endMonth = currentMonth.plusMonths(12),
        firstVisibleMonth = currentMonth
    )

    val today = LocalDate.now()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp)
    ) {

        Column {
            Text(
                "Your progress",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(6.dp))

            Text("Track your past days", color = textGray)

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(primaryGreen, RoundedCornerShape(50))
            )
        }

        Spacer(Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                HorizontalCalendar(
                    state = state,
                    dayContent = { calendarDay ->

                        val status = dayStatuses
                            .find { it.date == calendarDay.date }
                            ?.status

                        val isToday = calendarDay.date == today

                        val bgColor = when (status) {
                            Status.COMPLETED -> Color(0xFF6BCB9A)
                            Status.PARTIAL -> Color(0xFFFFD54F)
                            Status.NOT_COMPLETED -> Color(0xFFEF5350)
                            else -> Color.Transparent
                        }

                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(42.dp)
                                .background(
                                    color = bgColor,
                                    shape = CircleShape
                                )
                                .then(
                                    if (isToday) {
                                        Modifier.border(
                                            width = 2.dp,
                                            color = Color.Black,
                                            shape = CircleShape
                                        )
                                    } else Modifier
                                )
                                .clickable(
                                    enabled = status != null
                                ) {
                                    onDateClick(calendarDay.date)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                calendarDay.date.dayOfMonth.toString(),
                                color = if (status == null) textGray else Color.Black,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(Color(0xFF6BCB9A), "Completed")
                LegendItem(Color(0xFFFFD54F), "Partially\ncompleted")
                LegendItem(Color(0xFFEF5350), "Not completed")
            }
        }

        Spacer(Modifier.weight(1f))

        SecondaryButton(
            text = "Back to home",
            onClick = onGoHome
        )
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