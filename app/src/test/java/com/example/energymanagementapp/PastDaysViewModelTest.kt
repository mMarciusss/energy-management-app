package com.example.energymanagementapp

import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import com.example.energymanagementapp.viewmodel.PastDaysViewModel
import com.example.energymanagementapp.viewmodel.Status
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PastDaysViewModelTest {

    private lateinit var viewModel: PastDaysViewModel
    private val repository: PlanActivityRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should mark day as COMPLETED`() = runTest {
        val date = "2025-01-01"

        val activities = listOf(
            PlanActivityWithBreak(1, date, 1, true, null, "A", 3, null, null, null, null),
            PlanActivityWithBreak(2, date, 2, true, null, "B", 2, null, null, null, null)
        )

        coEvery { repository.getAllDates() } returns listOf(date)
        coEvery { repository.getPlanActivitiesWithBreaks(date) } returns activities

        viewModel = PastDaysViewModel(repository)

        viewModel.loadDayStatuses()

        advanceUntilIdle()

        assertEquals(Status.COMPLETED, viewModel.dayStatuses.first().status)
    }

    @Test
    fun `should mark day as PARTIAL`() = runTest {
        val date = "2025-01-02"

        val activities = listOf(
            PlanActivityWithBreak(1, date, 1, true, null, "A", 3, null, null, null, null),
            PlanActivityWithBreak(2, date, 2, false, null, "B", 2, null, null, null, null)
        )

        coEvery { repository.getAllDates() } returns listOf(date)
        coEvery { repository.getPlanActivitiesWithBreaks(date) } returns activities

        viewModel = PastDaysViewModel(repository)

        viewModel.loadDayStatuses()

        advanceUntilIdle()

        assertEquals(Status.PARTIAL, viewModel.dayStatuses.first().status)
    }

    @Test
    fun `should mark day as NOT_COMPLETED`() = runTest {
        val date = "2025-01-03"

        val activities = listOf(
            PlanActivityWithBreak(1, date, 1, false, null, "A", 3, null, null, null, null),
            PlanActivityWithBreak(2, date, 2, false, null, "B", 2, null, null, null, null)
        )

        coEvery { repository.getAllDates() } returns listOf(date)
        coEvery { repository.getPlanActivitiesWithBreaks(date) } returns activities

        viewModel = PastDaysViewModel(repository)

        viewModel.loadDayStatuses()

        advanceUntilIdle()

        assertEquals(Status.NOT_COMPLETED, viewModel.dayStatuses.first().status)
    }
}